package com.wolfscowl.ur_client.core.internal.arm

import com.wolfscowl.ur_client.core.internal.cmd_handler.CmdHandler
import com.wolfscowl.ur_client.core.internal.util.Util.createUniqueScriptName
import com.wolfscowl.ur_client.interfaces.state.ArmState
import com.wolfscowl.ur_client.core.internal.state.MutableArmState
import com.wolfscowl.ur_client.interfaces.arm.URArm
import com.wolfscowl.ur_client.model.element.Inertia
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.Vec3
import com.wolfscowl.ur_client.model.element.RunningState
import com.wolfscowl.ur_client.core.internal.event.URScriptEvent
import com.wolfscowl.ur_client.core.internal.state.MutableURScriptState
import com.wolfscowl.ur_client.model.element.Error
import com.wolfscowl.ur_client.model.robot_state.mode.RobotMode
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import sun.launcher.resources.launcher
import kotlin.coroutines.cancellation.CancellationException
import kotlin.jvm.java
import kotlin.math.PI
import kotlin.math.pow


internal class URArmImpl (
    private val ur: URArmController
): URArm {
    val urScriptEventFlow: SharedFlow<URScriptEvent> by lazy { ur.urScriptEventFlow }
    private val tcpPoseFlow: StateFlow<Pose?> by lazy { ur.tcpPoseFlow }
    private val jointPositionFlow: StateFlow<JointPosition?> by lazy { ur.jointPositionFlow }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    override fun enterFreeDriveMode(
        t: Float,
        cmdTimeout: Long,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val time = if(t>0.0f) t else 0.0f
        val scriptName = createUniqueScriptName("enter_freedrive_")
        val scriptTask = buildString {
            append("freedrive_mode()\n")
            append("sleep($time)")
        }
        val script = createScript(scriptName, scriptTask)
        return execute(scriptName,script,cmdTimeout,onChange,onFinished)
    }


    override fun exitFreeDriveMode(
        cmdTimeout: Long,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val scriptName = createUniqueScriptName("end_freedrive_")
        val scriptTask = buildString {
            append("end_freedrive_mode()\n")
        }
        val script = createScript(scriptName, scriptTask)
        return execute(scriptName,script,cmdTimeout,onChange,onFinished)
    }


    override fun setTcpOffset(
        p: Pose,
        cmdTimeout: Long,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val scriptName = createUniqueScriptName("set_tcp_offset_")
        val scriptTask = buildString {
            append("set_tcp(")
            append(p.toCmdString())
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask)
        return execute(scriptName,script,cmdTimeout,onChange,onFinished)
    }


    override fun setTargetPayload(
        m: Float,
        cog: Vec3?,
        inertia: Inertia?,
        cmdTimeout: Long,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val cog = cog?.toCmdString() ?: Vec3(0.00,0.00,0.00).toCmdString()
        val inertia = inertia?.toCmdString() ?: run {
            // calculate the inertia matrix for a sphere with 1g/cm3
            val volume = (m / 1000)
            val radius = ((3 * volume) / (4 * PI)).pow(1 / 3.toDouble())
            val i = (2 / 5.toDouble()) * m * (radius.pow(2))
            "[$i,$i,$i,0.000000,0.000000,0.000000]"
        }
        //log(inertia)
        val scriptName = createUniqueScriptName("set_target_payload_")
        val scriptTask = buildString {
            append("set_target_payload(")
            append("mass=").append(m).append(",")
            append("cog=").append(cog).append(",")
            append("inertia=").append(inertia)
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask)
        return execute(scriptName,script,cmdTimeout,onChange,onFinished)
    }


    override fun movej(
        q: JointPosition,
        a: Double,
        v: Double,
        t: Double,
        r: Double,
        cmdTimeout: Long,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val scriptName = createUniqueScriptName("movej_")
        val scriptTask = buildString {
            append("movej(")
            append(q.toCmdString()).append(", ")
            append("a=$a").append(", ")
            append("v=$v").append(", ")
            append("t=$t").append(", ")
            append("r=$r")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask)
        return execute(scriptName,script,cmdTimeout,onChange,onFinished)
    }

    override fun movej(
        p: Pose,
        a: Double,
        v: Double,
        t: Double,
        r: Double,
        cmdTimeout: Long,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val scriptName = createUniqueScriptName("movej_")
        val scriptTask = buildString {
            append("movej(")
            append(p.toCmdString()).append(", ")
            append("a=$a").append(", ")
            append("v=$v").append(", ")
            append("t=$t").append(", ")
            append("r=$r")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask)
        return execute(scriptName,script,cmdTimeout,onChange,onFinished)
    }


    override fun movel(
        p: Pose,
        a: Double,
        v: Double,
        t: Double,
        r: Double,
        cmdTimeout: Long,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val scriptName = createUniqueScriptName("movel_")
        val scriptTask = buildString {
            append("movel(")
            append(p.toCmdString()).append(", ")
            append("a=$a").append(", ")
            append("v=$v").append(", ")
            append("t=$t").append(", ")
            append("r=$r")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask)
        return execute(scriptName,script,cmdTimeout,onChange,onFinished)
    }

    override fun movel(
        q: JointPosition,
        a: Double,
        v: Double,
        t: Double,
        r: Double,
        cmdTimeout: Long,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val scriptName = createUniqueScriptName("movel_")
        val scriptTask = buildString {
            append("movel(")
            append(q.toCmdString()).append(", ")
            append("a=$a").append(", ")
            append("v=$v").append(", ")
            append("t=$t").append(", ")
            append("r=$r")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask)
        return execute(scriptName,script,cmdTimeout,onChange,onFinished)
    }


    override fun movec(
        positionVia: JointPosition,
        positionTo: JointPosition,
        a: Double,
        v: Double,
        r: Double,
        mode: Int,
        cmdTimeout: Long,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val scriptName = createUniqueScriptName("movec_")
        val scriptTask = buildString {
            append("movec(")
            append(positionVia.toCmdString()).append(", ")
            append(positionTo.toCmdString()).append(", ")
            append("a=$a").append(", ")
            append("v=$v").append(", ")
            append("r=$r").append(", ")
            append("mode=$mode")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask)
        return execute(scriptName,script,cmdTimeout,onChange,onFinished)
    }

    override fun movec(
        poseVia: Pose,
        poseTo: Pose,
        a: Double,
        v: Double,
        r: Double,
        mode: Int,
        cmdTimeout: Long,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val scriptName = createUniqueScriptName("movec_")
        val scriptTask = buildString {
            append("movec(")
            append(poseVia.toCmdString()).append(", ")
            append(poseTo.toCmdString()).append(", ")
            append("a=$a").append(", ")
            append("v=$v").append(", ")
            append("r=$r").append(", ")
            append("mode=$mode")
            append(")\n")
        }
        val script = createScript(scriptName, scriptTask)
        return execute(scriptName,script,cmdTimeout,onChange,onFinished)
    }


    private fun execute(
        scriptName: String,
        script: String,
        cmdTimeout: Long? = null,
        onChange: ((ArmState) -> Unit)?,
        onFinished: ((ArmState) -> Unit)?
    ): ArmState {
        val state = MutableArmState(scriptName = scriptName)
        val stateLock = Mutex()
        val collectStarted = CompletableDeferred<Unit>()
        val onCollectStarted = { collectStarted.complete(Unit) }
        CmdHandler.job?.cancel()

        CmdHandler.job = scope.launch {
            try {
                if (!ur.checkExecutionConditions(state)) {
                    launch(NonCancellable) { onChange?.invoke(state as ArmState) }
                    cancel()
                }
                launch {
                    collectStarted.await()
                    ur.sendURScript(script)
                }
                withCmdTimeout(cmdTimeout) {
                    val telemetryCollectorJob = launch {
                        collectTelemetryData { updateState ->
                            ur.updateAndNotify(this, state, stateLock, onChange) { currentState ->
                                updateState(currentState)
                            }
                        }
                    }
                    collectEvent(onCollectStarted, scriptName) { updateState ->
                            ur.updateAndNotify(this, state, stateLock, onChange) { currentState ->
                                updateState(currentState)
                            }
                    }
                    // Explicit cancellation of the other collects if collectEvent ends normally
                    telemetryCollectorJob.cancel()
                }
            } catch (e: TimeoutCancellationException) {
                state.runningState = RunningState.TIMEOUT
            } catch (e: CancellationException) {
                state.runningState = RunningState.CANCELED
            } finally {
                withContext(NonCancellable) {
                    onFinished?.invoke(state as ArmState)
                }
            }
        }
        return state
    }


    suspend inline fun <T> CoroutineScope.withCmdTimeout(
        timeout: Long?,
        crossinline execution: suspend CoroutineScope.() -> T
    ): T {
        return if (timeout != null) {
            withTimeout(timeout) {
                execution()
            }
        } else {
            execution()
        }
    }

    private suspend fun collectTelemetryData(
        updateState: suspend ((MutableArmState) -> Unit) -> Unit
    ) {
        jointPositionFlow.filterNotNull()
            .zip(tcpPoseFlow.filterNotNull()) { joinPosition, tcpPose ->
                Pair(joinPosition, tcpPose)
            }.collect { pair ->
            updateState {
                it.jointPosition = pair.first
                it.tcpPose = pair.second
            }
        }
    }


    private suspend fun collectEvent(
        onStart: () -> Boolean,
        scriptName: String,
        updateState: suspend ((MutableArmState) -> Unit) -> Unit
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
                    is URScriptEvent.TcpOffset -> {
                        if (event.scriptName == scriptName) {
                            updateState { it.tcpOffset = event.offset }
                        }
                    }
                    is URScriptEvent.Payload -> {
                        if (event.scriptName == scriptName) {
                            updateState { it.payload = event.payLoad }
                        }
                    }
                    is URScriptEvent.PayloadCog -> {
                        if (event.scriptName == scriptName)
                            updateState { it.payloadCog = event.cog }
                    }
                    is URScriptEvent.PayloadInertia -> {
                        if (event.scriptName == scriptName) {
                            updateState { it.payloadInertia = event.inertia }
                        }
                    }
                    else -> {}
                }
            }
    }


    private fun createScript(scriptName: String, scriptTask: String): String {
        val variables = mapOf(
            "{{_scriptName_}}" to scriptName,
            "{{_scriptTask_}}" to scriptTask
        )
        return this::class.java.getResource("/ur_arm.script")?.readText()?.let { content ->
            variables.entries.fold(content) { acc, (key, value) ->
                acc.replace(key, value)
            }
        } ?: ""
    }
}