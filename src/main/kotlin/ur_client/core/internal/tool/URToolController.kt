package com.wolfscowl.ur_client.core.internal.tool

import com.wolfscowl.ur_client.core.internal.event.URScriptEvent
import com.wolfscowl.ur_client.core.internal.state.MutableURScriptState
import com.wolfscowl.ur_client.model.robot_state.ToolModeInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex

internal interface URToolController{
    fun sendURScript(script: String)
    suspend fun checkExecutionConditions(state: MutableURScriptState): Boolean
    suspend fun <T : MutableURScriptState> updateAndNotify(
        scope: CoroutineScope, // Scope als Parameter statt Receiver
        state: T,
        stateLock: Mutex,
        onChange: ((T) -> Unit)?,
        updateState: (T) -> Unit
    )
    suspend fun <T> withCmdTimeout(
        timeout: Long?,
        execution: suspend CoroutineScope.() -> T
    ): T
    val toolModeInfoFlow: StateFlow<ToolModeInfo?>
    val urScriptEventFlow: SharedFlow<URScriptEvent>
}