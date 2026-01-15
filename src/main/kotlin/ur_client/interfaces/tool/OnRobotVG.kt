package com.wolfscowl.ur_client.interfaces.tool

import com.wolfscowl.ur_client.core.internal.config.Default
import com.wolfscowl.ur_client.interfaces.state.TFGToolState
import com.wolfscowl.ur_client.interfaces.state.VGToolGripState
import com.wolfscowl.ur_client.interfaces.state.VGToolReleaseState
import com.wolfscowl.ur_client.interfaces.state.VGToolSeekState

interface OnRobotVG: URTool {

    /**
     * Activate the vacuum gripper to pick up an object.
     *
     * This method represents the **grip** operation of the OnRobot **VGC10** and **VG10** vacuum gripper.
     * The gripper applies the specified vacuum level to grip an object.
     *
     * @param channel vacuum channel to activate (0 = A; 1 = B; 2 = AB).
     * @param vacuum target vacuum level (kPa).
     * @param gripTimeout maximum time (seconds) to wait for the object to be gripped.
     * @param cmdTimeout maximum time (milliseconds) to wait for the command to complete.
     * @param popupMsg enable/disable popup messages on Polyscope.
     * @param onChange ⚠️ **Experimental:** optional callback invoked whenever the [VGToolGripState]
     *   changes during execution. This API may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [VGToolGripState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [VGToolGripState] continuously updated execution state of the URScript.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotVGGrip
     */
    fun grip(
        channel: Int,
        vacuum: Int,
        gripTimeout: Float = Default.GRIP_TIMEOUT,
        cmdTimeout: Long? = null,
        popupMsg: Boolean = Default.POPUP_MSG,
        onChange: ((VGToolGripState) -> Unit)? = null,
        onFinished: ((VGToolGripState) -> Unit)? = null
    ): VGToolGripState


    /**
     * Seek and grip an object using the vacuum VG gripper by moving along the Z-axis.
     *
     * This method represents the **seek grip** operation of the OnRobot **VGC10** and **VG10** vacuum gripper.
     * The gripper moves downward along the Z-axis for a defined path length and searches for an object.
     * When a resistance is detected, the gripper automatically applies the specified vacuum to grip the object.
     * This is useful for locating and picking up objects that may not be precisely positioned.
     *
     * @param channel vacuum channel to activate (0 = A; 1 = B; 2 = AB).
     * @param vacuum target vacuum level (kPa) to apply once contact is detected.
     * @param gripTimeout maximum time (seconds) to wait for the object to be gripped.
     * @param cmdTimeout maximum time (milliseconds) to wait for the command to complete.
     * @param popupMsg enable/disable popup messages on Polyscope.
     * @param onChange ⚠️ **Experimental:** optional callback invoked whenever the [VGToolSeekState]
     *   changes during execution. This API may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [VGToolSeekState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [VGToolSeekState] continuously updated execution state of the URScript command.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotVGSeekGrip
     */
    fun seekGrip(
        channel: Int,
        vacuum: Int,
        gripTimeout: Float = Default.GRIP_TIMEOUT,
        cmdTimeout: Long? = null,
        popupMsg: Boolean = Default.POPUP_MSG,
        onChange: ((VGToolSeekState) -> Unit)? = null,
        onFinished: ((VGToolSeekState) -> Unit)? = null
    ): VGToolSeekState


    /**
     * Release an object held by the vacuum gripper.
     *
     * This method represents the **release** operation of the OnRobot **VGC10** and **VG10** vacuum grippers.
     * The gripper stops applying vacuum and safely releases the object.
     *
     * @param channel vacuum channel to deactivate (0 = A; 1 = B; 2 = AB).
     * @param releaseTimeout maximum time (seconds) to wait for the object to be released.
     * @param cmdTimeout maximum time (milliseconds) to wait for the command to complete.
     * @param popupMsg enable/disable popup messages on Polyscope.
     * @param onChange ⚠️ **Experimental:** optional callback invoked whenever the [VGToolReleaseState]
     *   changes during execution. This API may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [VGToolReleaseState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [VGToolReleaseState] continuously updated execution state of the URScript.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotVGRelease
     */
    fun release(
        channel: Int,
        releaseTimeout: Float = Default.GRIP_TIMEOUT,
        cmdTimeout: Long? = null,
        popupMsg: Boolean = Default.POPUP_MSG,
        onChange: ((VGToolReleaseState) -> Unit)? = null,
        onFinished: ((VGToolReleaseState) -> Unit)? = null
    ): VGToolReleaseState
}