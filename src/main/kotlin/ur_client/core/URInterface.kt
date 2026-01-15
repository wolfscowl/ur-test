package com.wolfscowl.ur_client.core

import com.wolfscowl.ur_client.interfaces.arm.URArm
import com.wolfscowl.ur_client.interfaces.state.ArmState
import com.wolfscowl.ur_client.interfaces.state.URScriptState
import com.wolfscowl.ur_client.interfaces.state.VGToolGripState
import com.wolfscowl.ur_client.interfaces.tool.OnRobotRG
import com.wolfscowl.ur_client.interfaces.tool.OnRobotTFG
import com.wolfscowl.ur_client.interfaces.tool.OnRobotVG
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
import com.wolfscowl.ur_client.model.robot_message.VersionMessage
import com.wolfscowl.ur_client.model.robot_state.mode.RobotMode
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface URInterface {
    val isConnected: Boolean
    val robotModeData: RobotModeData?
    val jointData: JointData?
    val jointPosition: JointPosition?
    val toolData: ToolData?
    val masterBoardData: MasterBoardData?
    val cartesianInfo: CartesianInfo?
    val tcpPose: Pose?
    val tcpOffset: Pose?
    val kinematicsInfo: KinematicsInfo?
    val configurationData: ConfigurationData?
    val forceModeData: ForceModeData?
    val additionalInfo: AdditionalInfo?
    val toolCommunicationInfo: ToolCommunicationInfo?
    val toolModeInfo: ToolModeInfo?
    val versionMessage: VersionMessage?
    val robotMessages: List<RobotMessage>
    val isConnectedFlow: StateFlow<Boolean>
    val robotModeDataFlow: StateFlow<RobotModeData?>
    val jointDataFlow: StateFlow<JointData?>
    val jointPositionFlow: StateFlow<JointPosition?>
    val toolDataFlow: StateFlow<ToolData?>
    val masterBoardDataFlow: StateFlow<MasterBoardData?>
    val cartesianInfoFlow: StateFlow<CartesianInfo?>
    val tcpPoseFlow: StateFlow<Pose?>
    val tcpOffsetFlow: StateFlow<Pose?>
    val kinematicsInfoFlow: StateFlow<KinematicsInfo?>
    val configurationDataFlow: StateFlow<ConfigurationData?>
    val forceModeDataFlow: StateFlow<ForceModeData?>
    val additionalInfoFlow: StateFlow<AdditionalInfo?>
    val toolCommunicationInfoFlow: StateFlow<ToolCommunicationInfo?>
    val toolModeInfoFlow: StateFlow<ToolModeInfo?>
    val robotMessagesFlow: SharedFlow<RobotMessage>

    val arm: URArm

    /**
     * Attaches an OnRobot RG gripper to the cobot.
     *
     * @param host The IP address of the OnRobot ComputeBox/EyeBox to which the tool is connected.
     * @param toolIndex The index of the tool (typically `0`, `1`, or `2`).
     * @return An initialized [OnRobotRG] instance bound to the robot, allowing the gripper to be controlled remotely.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotRGGrip
     */
    fun attachToolOnRobotRG(host: String, toolIndex: Int): OnRobotRG

    /**
     * Attaches an OnRobot TFG gripper to the cobot.
     *
     * @param host The IP address of the OnRobot ComputeBox/EyeBox to which the tool is connected.
     * @param toolIndex The index of the tool (typically `0`, `1`, or `2`).
     * @return An initialized [OnRobotTFG] instance bound to the robot, allowing the gripper to be controlled remotely.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotTFGGripExt
     */
    fun attachToolOnRobotTFG(host: String, toolIndex: Int): OnRobotTFG

    /**
     * Attaches an OnRobot VG gripper to the cobot.
     *
     * @param host The IP address of the OnRobot ComputeBox/EyeBox to which the tool is connected.
     * @param toolIndex The index of the tool (typically `0`, `1`, or `2`).
     * @return An initialized [OnRobotVG] instance bound to the robot, allowing the gripper to be controlled remotely.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotVGGrip
     */
    fun attachToolOnRobotVG(host: String, toolIndex: Int): OnRobotVG

    /**
     * Executes a custom URScript on the cobot.
     *
     * The script is sent to the UR controller and executed asynchronously. Optional
     * callbacks allow tracking state changes and detecting when execution finishes.
     *
     * The provided script must contain **only the body** of the program.
     * Do **not** wrap it in a `def … end` block — the API automatically generates
     * and wraps the script inside its own function context before sending it to
     * the robot.
     *
     * Execution errors can be signaled to the [URScriptState] with the URScript function `send_event("error","title","message")`.
     *
     * @param script The URScript body to execute (without a `def` wrapper).
     * @param cmdTimeout Maximum time in milliseconds to wait for the command to complete.
     *   If this timeout is exceeded, the command is considered failed and the
     *   [URScriptState] will no longer be updated.
     * @param onChange ⚠️ **Experimental:** Optional callback invoked whenever the [URScriptState]
     *   changes during execution.
     *   This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [URScriptState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [URScriptState] representing the current URScript execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.runScript
     */
    fun runURScript(
        script: String,
        cmdTimeout: Long? = null,
        onChange: ((URScriptState) -> Unit)? = null,
        onFinished: ((URScriptState) -> Unit)? = null
    ): URScriptState

    /**
     * Establishes the connection to the Universal robots cobot via the primary interface
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.connection
     */
    fun connect()

    /**
     * Disconnect the Universal Robots cobot (primary interface)
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.connection
     */
    fun disconnect()
}