package com.wolfscowl.ur_client.interfaces.tool

import com.wolfscowl.ur_client.core.internal.config.Default
import com.wolfscowl.ur_client.interfaces.state.ArmState
import com.wolfscowl.ur_client.interfaces.state.RGToolState

interface OnRobotRG: URTool {

    /**
     * Move the OnRobot RG gripper to the target width.
     *
     * This method represents the **grip** operation of the OnRobot **RG2** and **RG6** grippers.
     * Although internally similar to [release], it is provided as a separate command for
     * clearer semantics and a better developer experience.
     *
     * @param width target gripper width (mm).
     * @param force gripping force (N).
     * @param depthComp enable/disable depth compensation (Z-axis adjustment during closing)
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the [RGToolState] will no longer be updated.
     * @param popupMsg enable/disable popup messages on Polyscope.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [RGToolState]
     * @param onFinished optional callback that is invoked once when the [RGToolState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     *   changes during execution. This API is experimental and may change or be removed in future versions.
     * @return [RGToolState] continuously updated execution state of the URScript
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotRGGrip
     */
    fun grip(
        width: Double,
        force: Int,
        depthComp: Boolean = false,
        cmdTimeout: Long? = null,
        popupMsg: Boolean = Default.POPUP_MSG,
        onChange: ((RGToolState) -> Unit)? = null,
        onFinished: ((RGToolState) -> Unit)? = null
    ): RGToolState


    /**
     * Move the OnRobot RG gripper to the target width.
     *
     * This method represents the **release** operation of the OnRobot **RG2** and **RG6** grippers.
     * Although internally similar to [grip], it is provided as a separate command for
     * clearer semantics and a better developer experience.
     *
     * @param width target gripper width (mm).
     * @param force gripping force (N).
     * @param depthComp enable/disable depth compensation (Z-axis adjustment during opening)
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     *   If this time is exceeded, the command is considered failed and the [RGToolState] will no longer be updated.
     * @param popupMsg enable/disable popup messages on Polyscope.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [RGToolState]
     *   changes during execution. This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [RGToolState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [RGToolState] continuously updated execution state of the URScript.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotRGRelease
     */
    fun release(
        width: Double,
        force: Int,
        depthComp: Boolean = false,
        cmdTimeout: Long? = null,
        popupMsg: Boolean = Default.POPUP_MSG,
        onChange: ((RGToolState) -> Unit)? = null,
        onFinished: ((RGToolState) -> Unit)? = null
    ): RGToolState
}