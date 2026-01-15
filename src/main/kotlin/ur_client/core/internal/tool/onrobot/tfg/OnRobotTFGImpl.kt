package com.wolfscowl.ur_client.core.internal.tool.onrobot.tfg

import com.wolfscowl.ur_client.core.internal.util.Util.createUniqueScriptName
import com.wolfscowl.ur_client.core.internal.state.MutableTFGToolState
import com.wolfscowl.ur_client.interfaces.state.TFGToolState
import com.wolfscowl.ur_client.core.internal.tool.URToolController
import com.wolfscowl.ur_client.core.internal.tool.onrobot.OnRobotTool
import com.wolfscowl.ur_client.interfaces.tool.OnRobotTFG
import com.wolfscowl.ur_client.core.internal.event.URScriptEvent
import com.wolfscowl.ur_client.model.element.Error
import com.wolfscowl.ur_client.model.element.RunningState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.takeWhile

internal class OnRobotTFGImpl (
    override val host: String,
    override val toolIndex: Int,
    override val ur: URToolController,
) : OnRobotTool(), OnRobotTFG {
    override val model: String = "OnRobot 2FG"
    override val urScriptEventFlow: SharedFlow<URScriptEvent> by lazy { ur.urScriptEventFlow }

    override fun gripExt(
        width: Double,
        force: Int,
        speed: Int,
        cmdTimeout: Long?,
        popupMsg: Boolean,
        onChange: ((TFGToolState) -> Unit)?,
        onFinished: ((TFGToolState) -> Unit)?
    ): TFGToolState {
        val scriptName = createUniqueScriptName("twofg_ext_grip_")
        val scriptTask = buildString {
            append("twofg_grip(")
            append("width=$width").append(", ")
            append("force=$force").append(", ")
            append("speed=$speed").append(", ")
            append("external_grip=True").append(", ")
            append("stop_if_no_force=True").append(", ")
            append("tool_index=$toolIndex")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask, popupMsg)
        return execute(
            scriptName,
            script,
            cmdTimeout,
            MutableTFGToolState(scriptName),
            ::collectEvent,
            onChange,
            onFinished
        )
    }


    override fun gripInt(
        width: Double,
        force: Int,
        speed: Int,
        cmdTimeout: Long?,
        popupMsg: Boolean,
        onChange: ((TFGToolState) -> Unit)?,
        onFinished: ((TFGToolState) -> Unit)?
    ): TFGToolState {
        val scriptName = createUniqueScriptName("twofg_int_grip_")
        val scriptTask = buildString {
            append("twofg_grip(")
            append("width=$width").append(", ")
            append("force=$force").append(", ")
            append("speed=$speed").append(", ")
            append("external_grip=False").append(", ")
            append("stop_if_no_force=False").append(", ")
            append("tool_index=$toolIndex")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask, popupMsg)
        return execute(
            scriptName,
            script,
            cmdTimeout,
            MutableTFGToolState(scriptName),
            ::collectEvent,
            onChange,
            onFinished
        )
    }


    override fun releaseExt(
        width: Double,
        force: Int,
        speed: Int,
        cmdTimeout: Long?,
        popupMsg: Boolean,
        onChange: ((TFGToolState) -> Unit)?,
        onFinished: ((TFGToolState) -> Unit)?
    ): TFGToolState {
        val scriptName = createUniqueScriptName("twofg_ext_release_")
        val scriptTask = buildString {
            append("twofg_release(")
            append("width=$width").append(", ")
            append("force=$force").append(", ")
            append("speed=$speed").append(", ")
            append("external_release=True").append(", ")
            append("tool_index=$toolIndex")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask, popupMsg)
        return execute(
            scriptName,
            script,
            cmdTimeout,
            MutableTFGToolState(scriptName),
            ::collectEvent,
            onChange,
            onFinished
        )
    }


    override fun releaseInt(
        width: Double,
        force: Int,
        speed: Int,
        cmdTimeout: Long?,
        popupMsg: Boolean,
        onChange: ((TFGToolState) -> Unit)?,
        onFinished: ((TFGToolState) -> Unit)?
    ): TFGToolState {
        val scriptName = createUniqueScriptName("twofg_int_release_")
        val scriptTask = buildString {
            append("twofg_release(")
            append("width=$width").append(", ")
            append("force=$force").append(", ")
            append("speed=$speed").append(", ")
            append("external_release=False").append(", ")
            append("tool_index=$toolIndex")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask, popupMsg)
        return execute(
            scriptName,
            script,
            cmdTimeout,
            MutableTFGToolState(scriptName),
            ::collectEvent,
            onChange,
            onFinished
        )
    }


    private suspend fun collectEvent(
        onStart: () -> Boolean,
        scriptName: String,
        updateState: suspend ((MutableTFGToolState) -> Unit) -> Unit)
    {
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
                    is URScriptEvent.ToolDetection -> {
                        if (event.scriptName == scriptName)
                            updateState{ it.toolDetected = event.detected }
                    }
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
                    is URScriptEvent.GripDetection -> {
                        if (event.scriptName == scriptName)
                            updateState{ it.gripDetected = event.detected }
                    }
                    is URScriptEvent.ExtIntWidth -> {
                        if (event.scriptName == scriptName)
                            updateState{
                                it.extWidth = event.extWidth
                                it.intWidth = event.intWidth
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


    override fun createScript(scriptName: String, scriptTask: String, popupMsg: Boolean): String {
        val variables = mapOf(
            "{{_scriptName_}}" to scriptName,
            "{{_scriptTask_}}" to scriptTask,
            "{{_popupmsg_}}" to if (popupMsg) "True" else "False",
            "{{_host_}}" to host,
            "{{_toolIndex_}}" to toolIndex.toString(),
        )
        return this::class.java.getResource("/onrobot_tfg.script")?.readText()?.let { content ->
            variables.entries.fold(content) { acc, (key, value) ->
                acc.replace(key, value)
            }
        } ?: ""
    }


}