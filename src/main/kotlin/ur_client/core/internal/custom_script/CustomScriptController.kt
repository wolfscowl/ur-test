package com.wolfscowl.ur_client.core.internal.custom_script

import com.wolfscowl.ur_client.core.internal.event.URScriptEvent
import com.wolfscowl.ur_client.core.internal.state.MutableURScriptState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.sync.Mutex


internal interface CustomScriptController{
    suspend fun checkExecutionConditions(state: MutableURScriptState): Boolean
    suspend fun <T : MutableURScriptState> updateAndNotify(
        scope: CoroutineScope,
        state: T,
        stateLock: Mutex,
        onChange: ((T) -> Unit)?,
        updateState: (T) -> Unit
    )
    suspend fun <T> withCmdTimeout(
        timeout: Long?,
        execution: suspend CoroutineScope.() -> T
    ): T
    fun sendURScript(script: String)
    val urScriptEventFlow: SharedFlow<URScriptEvent>
}