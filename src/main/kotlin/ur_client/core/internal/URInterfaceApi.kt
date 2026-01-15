package com.wolfscowl.ur_client.core.internal

import com.wolfscowl.ur_client.core.URInterface
import com.wolfscowl.ur_client.core.internal.util.Util.log
import com.wolfscowl.ur_client.core.internal.arm.URArmImpl
import com.wolfscowl.ur_client.core.internal.arm.URArmController
import com.wolfscowl.ur_client.core.internal.decoder.Decoder
import com.wolfscowl.ur_client.core.internal.decoder.MessageType
import com.wolfscowl.ur_client.core.internal.tool.URToolController
import com.wolfscowl.ur_client.core.internal.custom_script.CustomScript
import com.wolfscowl.ur_client.core.internal.custom_script.CustomScriptController
import com.wolfscowl.ur_client.interfaces.arm.URArm
import com.wolfscowl.ur_client.interfaces.state.URScriptState
import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_state.AdditionalInfo
import com.wolfscowl.ur_client.model.robot_state.CartesianInfo
import com.wolfscowl.ur_client.model.robot_state.ConfigurationData
import com.wolfscowl.ur_client.model.robot_state.ForceModeData
import com.wolfscowl.ur_client.model.robot_state.JointData
import com.wolfscowl.ur_client.model.robot_state.KinematicsInfo
import com.wolfscowl.ur_client.model.robot_state.MasterBoardData
import com.wolfscowl.ur_client.model.robot_state.RobotModeData
import com.wolfscowl.ur_client.model.robot_state.ToolCommunicationInfo
import com.wolfscowl.ur_client.model.robot_state.ToolData
import com.wolfscowl.ur_client.model.robot_state.ToolModeInfo
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.core.internal.event.URScriptEvent
import com.wolfscowl.ur_client.core.internal.state.MutableURScriptState
import com.wolfscowl.ur_client.core.internal.tool.onrobot.rg.OnRobotRGImpl
import com.wolfscowl.ur_client.core.internal.tool.onrobot.tfg.OnRobotTFGImpl
import com.wolfscowl.ur_client.core.internal.tool.onrobot.vg.OnRobotVGImpl
import com.wolfscowl.ur_client.interfaces.tool.OnRobotRG
import com.wolfscowl.ur_client.interfaces.tool.OnRobotTFG
import com.wolfscowl.ur_client.interfaces.tool.OnRobotVG
import com.wolfscowl.ur_client.model.element.Error
import com.wolfscowl.ur_client.model.element.RunningState
import com.wolfscowl.ur_client.model.element.WatchdogConfig
import com.wolfscowl.ur_client.model.robot_message.KeyMessage
import com.wolfscowl.ur_client.model.robot_message.PopupMessage
import com.wolfscowl.ur_client.model.robot_message.RuntimeExceptionMessage
import com.wolfscowl.ur_client.model.robot_message.SafetyModeMessage
import com.wolfscowl.ur_client.model.robot_message.TextMessage
import com.wolfscowl.ur_client.model.robot_message.VersionMessage
import com.wolfscowl.ur_client.model.robot_state.RobotState
import com.wolfscowl.ur_client.model.robot_state.mode.RobotMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime


internal class URInterfaceApi(
    private val host: String,
    private val interfacePort: Int,
    private val connectTimeout: Int,
    private val watchdogConfig: WatchdogConfig,
    private val logRobotStates: Boolean,
    private val logRobotMessages: Boolean
) : URInterface, URArmController, URToolController, CustomScriptController {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var urInterface: Socket? = null
    private var decoder: Decoder? = null
     // 5 Pakete bei 10Hz
    private var watchdog: Watchdog = Watchdog(
        scope = scope,
        config = watchdogConfig,
        connect = ::connect,
        disconnect = ::disconnect
    )
    private var readJob: Job? = null
    private var writeJob: Job? = null

    override val arm: URArm = URArmImpl(this)
    private val customScript = CustomScript(this)

    override val isConnected: Boolean
        get() = urInterface != null
    @Volatile
    override var robotModeData: RobotModeData? = null
        private set
    @Volatile
    override var jointData: JointData? = null
        private set
    @Volatile
    override var jointPosition: JointPosition? = null
        private set
    @Volatile
    override var toolData: ToolData? = null
        private set
    @Volatile
    override var masterBoardData: MasterBoardData? = null
        private set
    @Volatile
    override var cartesianInfo: CartesianInfo? = null
        private set
    @Volatile
    override var tcpPose: Pose? = null
        private set
    @Volatile
    override var tcpOffset: Pose? = null
        private set
    @Volatile
    override var kinematicsInfo: KinematicsInfo? = null
        private set
    @Volatile
    override var configurationData: ConfigurationData? = null
        private set
    @Volatile
    override var forceModeData: ForceModeData? = null
        private set
    @Volatile
    override var additionalInfo: AdditionalInfo? = null
        private set
    @Volatile
    override var toolCommunicationInfo: ToolCommunicationInfo? = null
        private set
    @Volatile
    override var toolModeInfo: ToolModeInfo? = null
        private set
    @Volatile
    override var versionMessage: VersionMessage? = null
        private set
    private val _robotMessages: MutableList<RobotMessage> = mutableListOf()
    override val robotMessages: List<RobotMessage>
        get() = _robotMessages.toList()


    private val _isConnectedFlow = MutableStateFlow<Boolean>(false)
    override val isConnectedFlow: StateFlow<Boolean> = _isConnectedFlow
    private val _robotModeDataFlow = MutableStateFlow<RobotModeData?>(null)
    override val robotModeDataFlow: StateFlow<RobotModeData?> = _robotModeDataFlow
    private val _jointDataFlow = MutableStateFlow<JointData?>(null)
    override val jointDataFlow: StateFlow<JointData?> = _jointDataFlow
    private val _jointPositionFlow = MutableStateFlow<JointPosition?>(null)
    override val jointPositionFlow: StateFlow<JointPosition?> = _jointPositionFlow
    private val _toolDataFlow = MutableStateFlow<ToolData?>(null)
    override val toolDataFlow: StateFlow<ToolData?> = _toolDataFlow
    private val _masterBoardDataFlow = MutableStateFlow<MasterBoardData?>(null)
    override val masterBoardDataFlow: StateFlow<MasterBoardData?> = _masterBoardDataFlow
    private val _cartesianInfoFlow = MutableStateFlow<CartesianInfo?>(null)
    override val cartesianInfoFlow: StateFlow<CartesianInfo?> = _cartesianInfoFlow
    private val _tcpPoseFlow = MutableStateFlow<Pose?>(null)
    override val tcpPoseFlow: StateFlow<Pose?> = _tcpPoseFlow
    private val _tcpOffsetFlow = MutableStateFlow<Pose?>(null)
    override val tcpOffsetFlow: StateFlow<Pose?> = _tcpOffsetFlow
    private val _kinematicsInfoFlow = MutableStateFlow<KinematicsInfo?>(null)
    override val kinematicsInfoFlow: StateFlow<KinematicsInfo?> = _kinematicsInfoFlow
    private val _configurationDataFlow = MutableStateFlow<ConfigurationData?>(null)
    override val configurationDataFlow: StateFlow<ConfigurationData?> = _configurationDataFlow
    private val _forceModeDataFlow = MutableStateFlow<ForceModeData?>(null)
    override val forceModeDataFlow: StateFlow<ForceModeData?> = _forceModeDataFlow
    private val _additionalInfoFlow = MutableStateFlow<AdditionalInfo?>(null)
    override val additionalInfoFlow: StateFlow<AdditionalInfo?> = _additionalInfoFlow
    private val _toolCommunicationInfoFlow = MutableStateFlow<ToolCommunicationInfo?>(null)
    override val toolCommunicationInfoFlow: StateFlow<ToolCommunicationInfo?> = _toolCommunicationInfoFlow
    private val _toolModeInfoFlow = MutableStateFlow<ToolModeInfo?>(null)
    override val toolModeInfoFlow: StateFlow<ToolModeInfo?> = _toolModeInfoFlow
    private val _robotMessagesFlow = MutableSharedFlow<RobotMessage>(
        replay = 0,
        extraBufferCapacity = 20,
        onBufferOverflow = BufferOverflow.DROP_OLDEST  // ToDO -> Maybe change to BufferOverflow.SUSPEND and change tryEmit to emit with coroutine
    )
    override val robotMessagesFlow: SharedFlow<RobotMessage> = _robotMessagesFlow
    private val _urScriptEventFlow = MutableSharedFlow<URScriptEvent>(
        replay = 0,
        extraBufferCapacity = 20,
        onBufferOverflow = BufferOverflow.DROP_OLDEST  // ToDO -> Maybe change to BufferOverflow.SUSPEND and change tryEmit to emit with coroutine
    )
    // hidden flow - only for internal ArmController, ToolController and CustomScriptController
    override val urScriptEventFlow: SharedFlow<URScriptEvent> = _urScriptEventFlow

    override fun connect() {
        if (!isConnected) {
            try {
                urInterface = Socket().apply {
                    soTimeout = 500
                    connect(InetSocketAddress(host, interfacePort), connectTimeout)
                }
                _isConnectedFlow.value = true
                println("\uD83D\uDFE2 UR connected")
                startURJobs()
            } catch (e: Exception) {
                disconnect()
                println("⚠️ UR disconnected cause: $e")
                throw e
            }
        }
    }

    override fun disconnect() {
        if (isConnected) {
            readJob?.cancel()
            writeJob?.cancel()
            urInterface?.close()
            urInterface = null
            _isConnectedFlow.value = false
            println("\uD83D\uDD34 UR disconnected")
        }
    }


    private fun startURJobs() {
        watchdog.start()

        decoder = Decoder(urInterface?.getInputStream()
                ?: throw RuntimeException("Decoder input stream null"),
        )

        readJob = scope.launch {
            try {
                while (isActive) {
                    try {
                        step()
                        watchdog.heartbeat()
                    } catch (e: Exception) {
                        println("⚠️ ReadJob exception: $e")
                        disconnect()
                        break
                    }
                }
            } finally {
                watchdog.stop()
            }
        }
    }

    /***********************************************************************************************
                                            0.0 HELPER FUNCTIONS
     ***********************************************************************************************/

    private fun logRobotState(state: RobotState) {
        if (logRobotStates)
            println("$state\n")
    }

    private fun logRobotMessage(message: RobotMessage) {
        if (logRobotMessages)
            println("$message\n")
    }

    /***********************************************************************************************
                                            1.0 TOOL FUNCTIONS
     ***********************************************************************************************/

    override fun attachToolOnRobotRG(host: String, toolIndex: Int): OnRobotRG =
        OnRobotRGImpl(host = host, toolIndex = toolIndex, ur = this as URToolController)

    override fun attachToolOnRobotTFG(host: String, toolIndex: Int): OnRobotTFG =
        OnRobotTFGImpl(host = host, toolIndex = toolIndex, ur = this as URToolController)

    override fun attachToolOnRobotVG(host: String, toolIndex: Int): OnRobotVG =
        OnRobotVGImpl(host = host, toolIndex = toolIndex, ur = this as URToolController)


    /***********************************************************************************************
                                            2.0 UR-SCRIPT
     ***********************************************************************************************/

    override fun runURScript(
        script: String,
        cmdTimeout: Long?,
        onChange: ((URScriptState) -> Unit)?,
        onFinished: ((URScriptState) -> Unit)?
    ): URScriptState {
        return customScript.execute(
            scriptTask = script,
            cmdTimeout = cmdTimeout,
            onChange = onChange,
            onFinished = onFinished
        )
    }

    override suspend fun checkExecutionConditions(state: MutableURScriptState): Boolean {
        if (!isConnected) {
            state.errors.add(
                Error(
                    title = "UR Connection Error",
                    message = "The robot is not connected via the primary interface."
                )
            )
        }
        val modeData = withTimeoutOrNull(2000) { robotModeDataFlow.first{ it != null } }
        if (modeData == null) {
            state.errors.add(
                Error(
                    title = "Robot Mode Error",
                    message = "Robot mode data is unavailable."
                )
            )
        } else {
            if (modeData.robotMode != RobotMode.ROBOT_MODE_RUNNING) {
                state.errors.add(
                    Error(
                        title = "Robot Mode Error",
                        message = "The robot is in mode ${modeData.robotMode.name}."
                    )
                )
            }
            if (modeData.securityStopped || modeData.emergencyStopped) {
                state.errors.add(
                    Error(
                        title = "Safety Mode Error",
                        message = "Safety mode stop is active."
                    )
                )
                state.safetyModeStop = true
            }
            if (modeData.emergencyStopped) {
                state.errors.add(
                    Error(
                        title = "Safety Mode Error",
                        message = "The robot is still in emergency stop."
                    )
                )
                state.safetyModeStop = true
            }
        }
        if (state.errorOccurred) {
            state.runningState = RunningState.CANCELED
            return false
        }
        return true
    }


    override suspend fun <T> withCmdTimeout(
        timeout: Long?,
        execution: suspend CoroutineScope.() -> T
    ): T = coroutineScope {
        if (timeout != null && timeout > 0) {
            withTimeout(timeout) {
                execution()
            }
        } else {
            execution()
        }
    }


     override suspend fun <T : MutableURScriptState> updateAndNotify(
         scope: CoroutineScope,
         state: T,
         stateLock: Mutex,
         onChange: ((T) -> Unit)?,
         updateState: (T) -> Unit
    ) {
        stateLock.withLock {
            val oldState = state.copy()
            updateState(state)
            val newState = state.copy()
            if (oldState != newState)
                onChange?.let { scope.launch(NonCancellable) { it.invoke(newState as T) } }
        }
    }

    /***********************************************************************************************
                                    3.0 WRITE FUNCTIONS
     ***********************************************************************************************/

    override fun sendURScript(script: String) {
        if (isConnected) {
            scope.launch {
                writeJob?.join()
                writeJob = launch {
                    if (isConnected) {
                        urInterface?.outputStream?.let {
                            PrintWriter(it, true).println(script)
                        }
                    }
                }
            }
        }
    }


    /***********************************************************************************************
                                        4.0 READ FUNCTIONS
     ***********************************************************************************************/

    private fun step() {
        val messageType = decoder?.decodeMessage()
        when (messageType) {
            is MessageType.RobotMessage ->  { handleRobotMessage(messageType) }
            is MessageType.RobotProgramState -> { handleProgramState(messageType) }
            is MessageType.RobotState -> { handleRobotState(messageType) }
            else -> {
                // log("Skip Main Message ->   Code: ${messageType?.code}   Size: ${messageType?.payload?.size}")
            }
        }
    }


    /***********************************************************************************************
                                        4.1 ROBOT MESSAGE
     ***********************************************************************************************/

    private fun handleRobotMessage(message: MessageType.RobotMessage) {
        val robotMessage = decoder?.decodeRobotMessage(message)
        robotMessage?.let { handleRobotMessage(it) }
    }


    private fun handleRobotMessage(message: RobotMessage) {
        _robotMessages.add(message)
        _robotMessagesFlow.tryEmit(message)

        logRobotMessage(message)

        tryToHandleAsCmdEvent(message)
        if (message is VersionMessage)
            versionMessage = message
    }


    private fun tryToHandleAsCmdEvent(message: RobotMessage) {
        when (message) {
            is RuntimeExceptionMessage -> { URScriptEvent.fromRuntimeExceptionMessage(message)}
            is KeyMessage -> URScriptEvent.fromKeyMessage(message)
            is TextMessage -> URScriptEvent.fromTextMessage(message)
            is SafetyModeMessage -> URScriptEvent.fromSafetyModeMessage(message)
            else -> null
        }?.let{_urScriptEventFlow.tryEmit(it)}
    }


    /***********************************************************************************************
                                 4.2 ROBOT STATE
     ***********************************************************************************************/

    private fun handleRobotState(message: MessageType.RobotState) {
        while(message.consumed < message.payload.size) {

            val robotSate = decoder?.decodeRobotStateMessage(message)

            when (robotSate) {
                is RobotModeData -> handleRobotState(robotSate)
                is JointData -> handleRobotState(robotSate)
                is ToolData -> handleRobotState(robotSate)
                is MasterBoardData -> handleRobotState(robotSate)
                is CartesianInfo -> handleRobotState(robotSate)
                is KinematicsInfo -> handleRobotState(robotSate)
                is ConfigurationData -> handleRobotState(robotSate)
                is ForceModeData -> handleRobotState(robotSate)
                is AdditionalInfo -> handleRobotState(robotSate)
                is ToolCommunicationInfo -> handleRobotState(robotSate)
                is ToolModeInfo -> handleRobotState(robotSate)
            }
        }

    }


    private fun handleRobotState(state: RobotModeData) {
        if (state != robotModeData) {
            robotModeData = state
            _robotModeDataFlow.value = state
            logRobotState(state)
        }
    }

    private fun handleRobotState(state: JointData) {
        if (state != jointData) {
            jointData = state
            _jointDataFlow.value = state
            logRobotState(state)
        }
        val newJointPosition = JointPosition(
            base = state.base.qActual,
            shoulder = state.shoulder.qActual,
            elbow = state.elbow.qActual,
            wrist1 = state.wrist1.qActual,
            wrist2 = state.wrist2.qActual,
            wrist3 = state.wrist3.qActual
        )
        if (newJointPosition != jointPosition) {
            jointPosition = newJointPosition
            _jointPositionFlow.value = newJointPosition
        }
    }


    private fun handleRobotState(state: ToolData) {
        if (state != toolData) {
            toolData = state
            _toolDataFlow.value = state
            logRobotState(state)
        }
    }


    private fun handleRobotState(state: MasterBoardData) {
        if (state != masterBoardData) {
            masterBoardData = state
            _masterBoardDataFlow.value = state
            logRobotState(state)
        }
    }


    private fun handleRobotState(state: CartesianInfo) {
        if (state != cartesianInfo) {
            cartesianInfo = state
            _cartesianInfoFlow.value = state
            logRobotState(state)
        }
        val newTcpPose = Pose(
         x = state.x, y = state.y, z = state.z,
         rx = state.rX, ry = state.rY, rz = state.rZ
        )
        if (newTcpPose != tcpPose) {
            tcpPose = newTcpPose
            _tcpPoseFlow.value = newTcpPose
        }
        val newTcpOffset = Pose(
            x = state.tcpOffsetX, y = state.tcpOffsetY, z = state.tcpOffsetZ,
            rx = state.tcpOffsetRx, ry = state.tcpOffsetRy, rz = state.tcpOffsetRz
        )
        if (newTcpOffset != tcpOffset) {
            tcpOffset = newTcpOffset
            _tcpOffsetFlow.value = newTcpOffset
        }
    }


    private fun handleRobotState(state: KinematicsInfo) {
        if (state != kinematicsInfo) {
            kinematicsInfo = state
            _kinematicsInfoFlow.value = state
            logRobotState(state)
        }
    }


    private fun handleRobotState(state: ConfigurationData) {
        if (state != configurationData) {
            configurationData = state
            _configurationDataFlow.value = state
            logRobotState(state)
        }
    }


    private fun handleRobotState(state: ForceModeData) {
        if (state != forceModeData) {
            forceModeData = state
            _forceModeDataFlow.value = state
            logRobotState(state)
        }
    }


    private fun handleRobotState(state: AdditionalInfo) {
        if (state != additionalInfo) {
            additionalInfo = state
            _additionalInfoFlow.value = state
            logRobotState(state)
        }
    }


    private fun handleRobotState(state: ToolCommunicationInfo) {
        if (state != toolCommunicationInfo) {
            toolCommunicationInfo = state
            _toolCommunicationInfoFlow.value = state
            logRobotState(state)
        }
    }


    private fun handleRobotState(state: ToolModeInfo) {
        if (state != toolModeInfo) {
            toolModeInfo = state
            _toolModeInfoFlow.value = state
            logRobotState(state)
        }
    }


    /***********************************************************************************************
                                        4.3 PROGRAM STATE
     ***********************************************************************************************/

    private fun handleProgramState(message: MessageType.RobotProgramState) {
        val robotProgramSate = decoder?.decodeRobotProgramStateMessage(message)
        when(robotProgramSate) {
            null -> { /* TODO : pending implementation */   }
        }
    }


}