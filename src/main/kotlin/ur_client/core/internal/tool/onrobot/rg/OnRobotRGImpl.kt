package com.wolfscowl.ur_client.core.internal.tool.onrobot.rg

import com.wolfscowl.ur_client.core.internal.util.Util.createUniqueScriptName
import com.wolfscowl.ur_client.core.internal.state.MutableRGToolState
import com.wolfscowl.ur_client.interfaces.state.RGToolState
import com.wolfscowl.ur_client.core.internal.tool.URToolController
import com.wolfscowl.ur_client.core.internal.tool.onrobot.OnRobotTool
import com.wolfscowl.ur_client.interfaces.tool.OnRobotRG
import com.wolfscowl.ur_client.core.internal.event.URScriptEvent
import com.wolfscowl.ur_client.model.element.Error
import com.wolfscowl.ur_client.model.element.RunningState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.takeWhile
import javax.swing.Spring.width


internal class OnRobotRGImpl (
    override val host: String,
    override val toolIndex: Int,
    override val ur: URToolController
) : OnRobotTool(), OnRobotRG {
    override val model: String = "OnRobot RG"
    override val urScriptEventFlow: SharedFlow<URScriptEvent> by lazy { ur.urScriptEventFlow }

    override fun grip(
        width: Double,
        force: Int,
        depthComp: Boolean,
        cmdTimeout: Long?,
        popupMsg: Boolean,
        onChange: ((RGToolState) -> Unit)?,
        onFinished: ((RGToolState) -> Unit)?
    ): RGToolState {
        val scriptName = createUniqueScriptName("rg_grip_")
        val scriptTask = buildString {
            append("rg_grip(")
            append("width=$width").append(", ")
            append("force=$force").append(", ")
            append("tool_index=$toolIndex").append(", ")
            append("blocking=True").append(", ")
            append("depth_comp=").append(if (depthComp) "True" else "False")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask, popupMsg)
        return execute(
            scriptName,
            script,
            cmdTimeout,
            MutableRGToolState(scriptName),
            ::collectEvent,
            onChange,
            onFinished
        )
    }

    override fun release(
        width: Double,
        force: Int,
        depthComp: Boolean,
        cmdTimeout: Long?,
        popupMsg: Boolean,
        onChange: ((RGToolState) -> Unit)?,
        onFinished: ((RGToolState) -> Unit)?
    ): RGToolState {
        val scriptName = createUniqueScriptName("rg_release_")
        val scriptTask = buildString {
            append("rg_release(")
            append("width=$width").append(", ")
            append("force=$force").append(", ")
            append("tool_index=$toolIndex").append(", ")
            append("blocking=True").append(", ")
            append("depth_comp=").append(if (depthComp) "True" else "False")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask, popupMsg)
        return execute(
            scriptName,
            script,
            cmdTimeout,
            MutableRGToolState(scriptName),
            ::collectEvent,
            onChange,
            onFinished
        )
    }


    private suspend fun collectEvent(
        onStart: () -> Boolean,
        scriptName: String,
        updateState: suspend ((MutableRGToolState) -> Unit) -> Unit
    ) {
        urScriptEventFlow
            .onStart { onStart() }
            .takeWhile { event ->
                when (event) {
                    is URScriptEvent.ExecutionState -> {
                        updateState { it.runningState = event.state }
                        !(event.scriptName == scriptName && event.state in setOf(
                            RunningState.END,
                            RunningState.PAUSED
                        ))
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
                    is URScriptEvent.GripDetection -> {
                        if (event.scriptName == scriptName)
                            updateState { it.gripDetected = event.detected }
                    }
                    is URScriptEvent.WidthDepth -> {
                        if (event.scriptName == scriptName)
                            updateState {
                                it.width = event.width
                                it.depth = event.depth
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
        return this::class.java.getResource("/onrobot_rg.script")?.readText()?.let { content ->
            variables.entries.fold(content) { acc, (key, value) ->
                acc.replace(key, value)
            }
        } ?: ""
    }


}


