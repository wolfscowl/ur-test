package com.wolfscowl.ur_client.core.internal.arm

import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.core.internal.event.URScriptEvent
import com.wolfscowl.ur_client.core.internal.state.MutableURScriptState
import com.wolfscowl.ur_client.interfaces.state.ArmState
import com.wolfscowl.ur_client.model.robot_state.RobotModeData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex

internal interface URArmController{
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
    val urScriptEventFlow: SharedFlow<URScriptEvent>
    val jointPositionFlow: StateFlow<JointPosition?>
    val tcpPoseFlow: StateFlow<Pose?>

}