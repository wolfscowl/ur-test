package com.wolfscowl.ur_client.core.internal.custom_script

import com.wolfscowl.ur_client.core.internal.cmd_handler.CmdHandler
import com.wolfscowl.ur_client.core.internal.config.Default
import com.wolfscowl.ur_client.core.internal.state.MutableURScriptState
import com.wolfscowl.ur_client.core.internal.util.Util.createUniqueScriptName
import com.wolfscowl.ur_client.interfaces.state.URScriptState
import com.wolfscowl.ur_client.model.element.RunningState
import com.wolfscowl.ur_client.core.internal.event.URScriptEvent
import com.wolfscowl.ur_client.core.internal.state.MutableArmState
import com.wolfscowl.ur_client.core.internal.state.MutableToolState
import com.wolfscowl.ur_client.interfaces.state.ArmState
import com.wolfscowl.ur_client.model.element.Error
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.cancellation.CancellationException

internal class CustomScript(
    private val ur: CustomScriptController
) {
    val urScriptEventFlow: SharedFlow<URScriptEvent> by lazy { ur.urScriptEventFlow }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun execute(
        scriptTask: String,
        cmdTimeout: Long? = null,
        onChange: ((URScriptState) -> Unit)?,
        onFinished: ((URScriptState) -> Unit)?
    ): URScriptState {
        val scriptName = createUniqueScriptName("custom_script_")
        val script = createScript(scriptName, scriptTask)
        val state = MutableURScriptState(scriptName = scriptName)
        val stateLock = Mutex()
        val collectStarted = CompletableDeferred<Unit>()
        val onCollectStarted = { collectStarted.complete(Unit) }
        CmdHandler.job?.cancel()

        CmdHandler.job = scope.launch {
            try {
                if (!ur.checkExecutionConditions(state)) {
                    launch(NonCancellable) { onChange?.invoke(state as URScriptState) }
                    cancel()
                }
                launch {
                    collectStarted.await()
                    ur.sendURScript(script)
                }
                ur.withCmdTimeout(cmdTimeout) {
                    collectEvent(onCollectStarted, scriptName) { updateState ->
                        ur.updateAndNotify(this, state, stateLock, onChange) { currentState ->
                            updateState(currentState)
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                state.runningState = RunningState.TIMEOUT
            } catch (e: CancellationException) {
                state.runningState = RunningState.CANCELED
            } finally {
                withContext(NonCancellable) {
                    onFinished?.invoke(state as URScriptState)
                }
            }
        }
        return state
    }




    private suspend fun collectEvent(
        onStart: () -> Boolean,
        scriptName: String,
        updateState: suspend ((MutableURScriptState) -> Unit) -> Unit
    ) {
        urScriptEventFlow
            .onStart { onStart() }
            .takeWhile { event ->
                when (event) {
                    is URScriptEvent.ExecutionState -> {
                        updateState { it.runningState = event.state }
                        !(event.scriptName == scriptName && event.state in setOf(RunningState.END, RunningState.PAUSED))
                    }
                    is URScriptEvent.RuntimeException -> {
                        updateState {
                            it.errors.add(event.toError())
                            it.runningState = RunningState.END
                        }
                        false
                    }
                    else -> true
                }
            }
            .collect { event ->
                when(event) {
                    is URScriptEvent.SafetyModeStop -> {
                        updateState{
                            it.safetyModeStop = true
                            it.errors.add(
                                Error(
                                    title = "Safety Mode Error",
                                    message = "A safety mode stop has been triggered."
                                )
                            )
                        }
                    }
                    is URScriptEvent.ScriptError -> {
                        if (event.scriptName == scriptName)
                            updateState { it.errors.add(event.toError()) }
                    }
                    else -> {}
                }
            }
    }


    private fun createScript(scriptName: String, scriptTask: String): String {
        val scriptTask = scriptTask.lines().joinToString("\n") {"\t$it"}
        val variables = mapOf(
            "{{_scriptName_}}" to scriptName,
            "{{_scriptTask_}}" to scriptTask
        )
        return this::class.java.getResource("/ur_script.script")?.readText()?.let { content ->
            variables.entries.fold(content) { acc, (key, value) ->
                acc.replace(key, value)
            }
        } ?: ""
    }


}