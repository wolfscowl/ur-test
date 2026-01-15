package com.wolfscowl.ur_client.core.internal.tool.onrobot.vg

import com.wolfscowl.ur_client.core.internal.util.Util.createUniqueScriptName
import com.wolfscowl.ur_client.core.internal.state.MutableVGToolGripState
import com.wolfscowl.ur_client.core.internal.state.MutableVGToolReleaseState
import com.wolfscowl.ur_client.core.internal.state.MutableVGToolSeekState
import com.wolfscowl.ur_client.core.internal.state.MutableVGToolState
import com.wolfscowl.ur_client.interfaces.state.VGToolGripState
import com.wolfscowl.ur_client.interfaces.state.VGToolReleaseState
import com.wolfscowl.ur_client.interfaces.state.VGToolSeekState
import com.wolfscowl.ur_client.core.internal.tool.URToolController
import com.wolfscowl.ur_client.core.internal.tool.onrobot.OnRobotTool
import com.wolfscowl.ur_client.interfaces.tool.OnRobotVG
import com.wolfscowl.ur_client.model.element.RunningState
import com.wolfscowl.ur_client.core.internal.event.URScriptEvent
import com.wolfscowl.ur_client.model.element.Error
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.takeWhile


internal class OnRobotVGImpl (
    override val host: String,
    override val toolIndex: Int,
    override val ur: URToolController
) : OnRobotTool(), OnRobotVG {
    override val model: String = "OnRobot VG"
    override val urScriptEventFlow: SharedFlow<URScriptEvent> by lazy { ur.urScriptEventFlow }

    override fun grip(
        channel: Int,
        vacuum: Int,
        gripTimeout: Float,
        cmdTimeout: Long?,
        popupMsg: Boolean,
        onChange: ((VGToolGripState) -> Unit)?,
        onFinished: ((VGToolGripState) -> Unit)?
    ): VGToolGripState {
        val scriptName = createUniqueScriptName("vg_grip_")
        val scriptTask = buildString {
            append("vg_grip(")
            append("channel=$channel").append(", ")
            append("vacuum=$vacuum").append(", ")
            append("timeout=$gripTimeout").append(", ")
            append("tool_index=$toolIndex")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask, popupMsg)
        return execute(
            scriptName,
            script,
            cmdTimeout,
            MutableVGToolGripState(scriptName),
            ::collectEvent,
            onChange,
            onFinished
        )
    }

    override fun seekGrip(
        channel: Int,
        vacuum: Int,
        gripTimeout: Float,
        cmdTimeout: Long?,
        popupMsg: Boolean,
        onChange: ((VGToolSeekState) -> Unit)?,
        onFinished: ((VGToolSeekState) -> Unit)?
    ): VGToolSeekState {
        val scriptName = createUniqueScriptName("vg_seek_grip_")
        val scriptTask = buildString {
            append("vg_seek_grip(")
            append("channel=$channel").append(", ")
            append("vacuum=$vacuum").append(", ")
            append("vacuum=$gripTimeout").append(", ")
            append("tool_index=$toolIndex")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask, popupMsg)
        return execute(
            scriptName,
            script,
            cmdTimeout,
            MutableVGToolSeekState(scriptName),
            ::collectEvent,
            onChange,
            onFinished
        )
    }

    override fun release(
        channel: Int,
        releaseTimeout: Float,
        cmdTimeout: Long?,
        popupMsg: Boolean,
        onChange: ((VGToolReleaseState) -> Unit)?,
        onFinished: ((VGToolReleaseState) -> Unit)?
    ): VGToolReleaseState {
        val scriptName = createUniqueScriptName("vg_release_")
        val scriptTask = buildString {
            append("vg_release(")
            append("channel=$channel").append(", ")
            append("timeout=$releaseTimeout").append(", ")
            append("tool_index=$toolIndex")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask, popupMsg)
        return execute(
            scriptName,
            script,
            cmdTimeout,
            MutableVGToolReleaseState(scriptName),
            ::collectEvent,
            onChange,
            onFinished
        )
    }


    private suspend fun collectEvent(
        onStart: () -> Boolean,
        scriptName: String,
        updateState: suspend ((MutableVGToolState) -> Unit) -> Unit
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
                when (event) {
                    is URScriptEvent.ToolDetection -> {
                        if (event.scriptName == scriptName)
                            updateState { it.toolDetected = event.detected }
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
                    is URScriptEvent.VacuumAB -> {
                        if (event.scriptName == scriptName)
                            updateState {
                                it.vacuumA = event.vacuumA
                                it.vacuumB = event.vacuumB
                            }
                    }
                    is URScriptEvent.ObjectDetection -> {
                        if (event.scriptName == scriptName)
                            updateState { state ->
                                if (state is MutableVGToolSeekState) {
                                    state.objectDetection = event.found
                                }
                            }
                    }
                    is URScriptEvent.VacuumReached -> {
                        if (event.scriptName == scriptName)
                            updateState { state ->
                                when (state) {
                                    is MutableVGToolGripState -> state.vacuumReached = event.state
                                    is MutableVGToolSeekState -> state.vacuumReached = event.state
                                    else -> { }
                                }
                            }
                    }
                    is URScriptEvent.VacuumReleased -> {
                        if (event.scriptName == scriptName)
                            updateState { state ->
                                if (state is MutableVGToolReleaseState) {
                                    state.vacuumReleased = event.state
                                }
                            }
                    }
                    is URScriptEvent.ScriptError -> {
                        if (event.scriptName == scriptName)
                            updateState { it.errors.add(event.toError()) }
                    }
                    else -> {  }
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
        return this::class.java.getResource("/onrobot_vg.script")?.readText()?.let { content ->
            variables.entries.fold(content) { acc, (key, value) ->
                acc.replace(key, value)
            }
        } ?: ""
    }


}