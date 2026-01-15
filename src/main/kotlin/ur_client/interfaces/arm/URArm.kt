package com.wolfscowl.ur_client.interfaces.arm

import com.wolfscowl.ur_client.core.internal.config.Default
import com.wolfscowl.ur_client.interfaces.state.ArmState
import com.wolfscowl.ur_client.model.element.Inertia
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.Vec3

interface URArm {



    /**
     * Enables freedrive mode, allowing the cobot arm to be moved manually by hand.
     * Note that any subsequent URScript command will automatically disable freedrive mode.
     *
     * @param t duration of the freedrive mode in seconds.
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the [ArmState] will no longer be updated.
     *   Ensure that [cmdTimeout] is greater than [t].
     * @param onChange ⚠️ **Experimental:**  optional callback that is invoked whenever the [ArmState] changes during execution.
     *   Can be used to track progress or react to state transitions in real time.
     * @param onFinished optional callback that is invoked once when the [ArmState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [ArmState] representing the current URScript command execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.freeDriveMode
     */
    fun enterFreeDriveMode(
        t: Float = 60.0f,
        cmdTimeout: Long = Default.CMD_TIMEOUT,
        onChange: ((ArmState) -> Unit)? = null,
        onFinished: ((ArmState) -> Unit)? = null
    ): ArmState

    /**
     * Disables freedrive mode, restoring normal robot control.
     * After executing this command, the cobot arm can no longer be moved manually by hand.
     *
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the [ArmState] will no longer be updated.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [ArmState] changes during execution.
     *   Can be used to track progress or react to state transitions in real time.
     * @param onFinished optional callback that is invoked once when the [ArmState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [ArmState] representing the current URScript command execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.freeDriveMode
     */
    fun exitFreeDriveMode(
        cmdTimeout: Long = Default.CMD_TIMEOUT,
        onChange: ((ArmState) -> Unit)? = null,
        onFinished: ((ArmState) -> Unit)? = null
    ): ArmState

    /**
     * Sets the active tcp offset, i.e. the transformation from the output flange coordinate system tot he
     * TCP as a pose.
     *
     * @param p A pose describing the transformation
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the [ArmState] will no longer be updated.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [ArmState] changes during execution.
     *   Can be used to track progress or react to state transitions in real time.
     * @param onFinished optional callback that is invoked once when the [ArmState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [ArmState] representing the current URScript command execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.setTcpOffset
     */
    fun setTcpOffset(
        p: Pose,
        cmdTimeout: Long = Default.CMD_TIMEOUT,
        onChange: ((ArmState) -> Unit)? = null,
        onFinished: ((ArmState) -> Unit)? = null
    ): ArmState

    /**
     *  Sets the mass, center of gravity (abbr. CoG) and the inertia matrix of the active payload.
     *  This function must be called when the payload mass,the mass displacement (CoG) or the inertia
     *  matrix changes - (i.e. when the robot picks up or puts down a workpiece)
     *
     * @param m mass in kilograms
     * @param cog Center of Gravity, a vector[CoGx,CoGy,CoGz] specifying the displacement (in meters) from
     *   the tool mount.
     * @param inertia payload inertia matrix (in kg*m^2), as a vector with six elements [Ixx,Iyy,Izz,Ixy,Ixz,Iyz]
     *  with origin in the CoG and the axes aligned with the tool flange axes. Default is a sphere with 1g/cm3.
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the [ArmState] will no longer be updated.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [ArmState] changes during execution.
     *   Can be used to track progress or react to state transitions in real time.
     * @param onFinished optional callback that is invoked once when the [ArmState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [ArmState] representing the current URScript command execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.setTargetPayload
     */
    fun setTargetPayload(
        m: Float = 0.0f,
        cog: Vec3? = null,
        inertia: Inertia? = null,
        cmdTimeout: Long = Default.CMD_TIMEOUT,
        onChange: ((ArmState) -> Unit)? = null,
        onFinished: ((ArmState) -> Unit)? = null
    ): ArmState

    /**
     * Move to position (linear in joint-space)
     *
     * @param q target joint position
     * @param a joint acceleration of leading axis (rad/s^2)
     * @param v joint speed of leading axis (rad/s)
     * @param t time (s)
     * @param r blend radius (m)
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the ArmState will no longer be updated.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [ArmState] changes during execution.
     *   Can be used to track progress or react to state transitions in real time.
     * @param onFinished optional callback that is invoked once when the [ArmState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [ArmState] representing the current URScript command execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.movejJoint
     */
    fun movej(
        q: JointPosition,
        a: Double = Default.ACCELERATION,
        v: Double = Default.VELOCITY,
        t: Double = Default.TIME,
        r: Double = Default.BLEND_RADIUS,
        cmdTimeout: Long = Default.CMD_TIMEOUT,
        onChange: ((ArmState) -> Unit)? = null,
        onFinished: ((ArmState) -> Unit)? = null
    ): ArmState

    /**
     * Move to position (linear in joint-space).
     *
     * @param p target pose of the TCP (tool center point).
     *    Then inverse kinematics is used to calculate the corresponding joint positions.
     * @param a joint acceleration of the leading axis (rad/s²).
     * @param v joint speed of the leading axis (rad/s).
     * @param t time to reach the target position (s).
     * @param r blend radius (m).
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the ArmState will no longer be updated.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [ArmState] changes during execution.
     *   This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [ArmState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [ArmState] representing the current URScript command execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.movejTCP
     */
    fun movej(
        p: Pose,
        a: Double = Default.ACCELERATION,
        v: Double = Default.VELOCITY,
        t: Double = Default.TIME,
        r: Double = Default.BLEND_RADIUS,
        cmdTimeout: Long = Default.CMD_TIMEOUT,
        onChange: ((ArmState) -> Unit)? = null,
        onFinished: ((ArmState) -> Unit)? = null
    ): ArmState


    /**
     * Move to position (linear in tool-space).
     *
     * @param p target pose of the TCP (tool center point).
     * @param a tool acceleration (m/s^2)
     * @param v tool speed (m/s)
     * @param t time to reach the target position (s).
     * @param r blend radius (m).
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the ArmState will no longer be updated.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [ArmState] changes during execution.
     *   This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [ArmState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [ArmState] representing the current URScript command execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.movelTCP
     */
    fun movel(
        p: Pose,
        a: Double = Default.ACCELERATION,
        v: Double = Default.VELOCITY,
        t: Double = Default.TIME,
        r: Double = Default.BLEND_RADIUS,
        cmdTimeout: Long = Default.CMD_TIMEOUT,
        onChange: ((ArmState) -> Unit)? = null,
        onFinished: ((ArmState) -> Unit)? = null
    ): ArmState


    /**
     * Move to position (linear in tool-space).
     *
     * @param q target joint positions. Then forward kinematics is used to calculate the corresponding pose.
     * @param a tool acceleration (m/s^2)
     * @param v tool speed (m/s)
     * @param t time to reach the target position (s).
     * @param r blend radius (m).
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the ArmState will no longer be updated.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [ArmState] changes during execution.
     *   This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [ArmState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [ArmState] representing the current URScript command execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.movelJoint
     */
    fun movel(
        q: JointPosition,
        a: Double = Default.ACCELERATION,
        v: Double = Default.VELOCITY,
        t: Double = Default.TIME,
        r: Double = Default.BLEND_RADIUS,
        cmdTimeout: Long = Default.CMD_TIMEOUT,
        onChange: ((ArmState) -> Unit)? = null,
        onFinished: ((ArmState) -> Unit)? = null
    ): ArmState

    /**
     * Move circular to position (circular in tool-space).
     * TCP moves on the circular arc segment from current pose, through [poseVia] to [poseTo].
     * Accelerates to and moves with constant toolspeed [v]. Use the mode parameter to define the
     * orientation interpolation.
     *
     * @param poseVia path point (note: only position is used. Rotations are not used so they can be left as zeros.)
     * @param poseTo target pose (note: only position is used in Fixed orientation mode).
     * @param a tool acceleration (m/s^2)
     * @param v tool speed (m/s)
     * @param r blend radius (m)
     * @param mode defines the orientation interpolation mode:
     * - `0`: Unconstrained mode. Interpolate orientation from current pose to target pose ([poseTo]) <br><br>
     * - `1`: Fixedmode. Keep orientation constant relative to the tangent of the circular arc (starting from current pose)
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the ArmState will no longer be updated.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [ArmState] changes during execution.
     *   This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [ArmState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [ArmState] representing the current URScript command execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.movecTCP
     */
    fun movec(
        poseVia: Pose,
        poseTo: Pose,
        a: Double = Default.ACCELERATION,
        v: Double = Default.VELOCITY,
        r: Double = Default.BLEND_RADIUS,
        mode: Int = 0,
        cmdTimeout: Long = Default.CMD_TIMEOUT,
        onChange: ((ArmState) -> Unit)? = null,
        onFinished: ((ArmState) -> Unit)? = null
    ): ArmState


    /**
     * Move circular to position (circular in tool-space).
     * TCP moves on the circular arc segment from current pose, through [positionVia] to [positionTo].
     * Accelerates to and moves with constant toolspeed [v]. Use the mode parameter to define the
     * orientation interpolation.
     *
     * @param positionVia path point as joint position. Then forward kinematics is used to calculate the corresponding pose.
     * @param positionTo target joint position. Then forward kinematics is used to calculate the corresponding pose.
     * @param a tool acceleration (m/s^2)
     * @param v tool speed (m/s)
     * @param r blend radius (m)
     * @param mode defines the orientation interpolation mode:
     * - `0`: Unconstrained mode. Interpolate orientation from current pose to target pose (poseTo) <br><br>
     * - `1`: Fixedmode. Keep orientation constant relative to the tangent of the circular arc (starting from current pose)
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the ArmState will no longer be updated.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [ArmState] changes during execution.
     *   This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [ArmState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [ArmState] representing the current URScript command execution state, which is continuously updated.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.movecJoint
     */
    fun movec(
        positionVia: JointPosition,
        positionTo: JointPosition,
        a: Double = Default.ACCELERATION,
        v: Double = Default.VELOCITY,
        r: Double = Default.BLEND_RADIUS,
        mode: Int = 0,
        cmdTimeout: Long = Default.CMD_TIMEOUT,
        onChange: ((ArmState) -> Unit)? = null,
        onFinished: ((ArmState) -> Unit)? = null
    ): ArmState


}