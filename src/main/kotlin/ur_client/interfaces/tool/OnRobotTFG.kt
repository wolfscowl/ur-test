package com.wolfscowl.ur_client.interfaces.tool

import com.wolfscowl.ur_client.core.internal.config.Default
import com.wolfscowl.ur_client.interfaces.state.RGToolState
import com.wolfscowl.ur_client.interfaces.state.TFGToolState


interface OnRobotTFG: URTool {

    /**
     * External gripping: close the 2FG gripper around an object from the outside.
     *
     * This method represents the **external grip** operation of the OnRobot **2FG7** and **2FG14** grippers.
     * The fingers move inward to grasp the object from the outside.
     * Although internally similar to [releaseExt], it is provided as a separate command for
     * clearer semantics and a better developer experience.
     *
     * @param width target finger distance (mm).
     * @param force gripping force (N).
     * @param speed gripping speed (mm/s).
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     * @param popupMsg enable/disable popup messages on Polyscope.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [TFGToolState]
     *   changes during execution. This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [TFGToolState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [TFGToolState] continuously updated execution state of the URScript.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotTFGGripExt
     */
    fun gripExt(
        width: Double,
        force: Int,
        speed: Int,
        cmdTimeout: Long? = null,
        popupMsg: Boolean = Default.POPUP_MSG,
        onChange: ((TFGToolState) -> Unit)? = null,
        onFinished: ((TFGToolState) -> Unit)? = null
    ): TFGToolState

    /**
     * Internal gripping: open the gripper 2FG to grip an object from the inside.
     *
     * This method represents the **internal grip** operation of the OnRobot **2FG7** and **2FG14** grippers.
     * The fingers move outward to press against the inner surfaces of a cavity, tube, or hole.
     * Although internally similar to [releaseInt], it is provided as a separate command for
     * clearer semantics and a better developer experience.
     *
     * @param width target finger distance (mm).
     * @param force gripping force (N).
     * @param speed gripping speed (mm/s).
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     * @param popupMsg enable/disable popup messages on Polyscope.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [TFGToolState]
     *   changes during execution. This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [TFGToolState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [TFGToolState] continuously updated execution state of the URScript.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotTFGGripInt
     */
    fun gripInt(
        width: Double,
        force: Int,
        speed: Int,
        cmdTimeout: Long? = null,
        popupMsg: Boolean = Default.POPUP_MSG,
        onChange: ((TFGToolState) -> Unit)? = null,
        onFinished: ((TFGToolState) -> Unit)? = null
    ): TFGToolState


    /**
     * External release: open the gripper 2FG to release an object held from the outside.
     *
     * This method represents the **external release** operation of the OnRobot **2FG7** and **2FG14** grippers.
     * The fingers move outward to let go of an object that was previously held externally.
     * Although internally similar to [gripExt], it is provided as a separate command for
     * clearer semantics and a better developer experience.
     *
     * @param width target finger distance (mm).
     * @param force release force (N).
     * @param speed release speed (mm/s).
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     * @param popupMsg enable/disable popup messages on Polyscope.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [TFGToolState]
     *   changes during execution. This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [TFGToolState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [TFGToolState] continuously updated execution state of the URScript.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotTFGReleaseExt
     */
    fun releaseExt(
        width: Double,
        force: Int,
        speed: Int,
        cmdTimeout: Long? = null,
        popupMsg: Boolean = Default.POPUP_MSG,
        onChange: ((TFGToolState) -> Unit)? = null,
        onFinished: ((TFGToolState) -> Unit)? = null
    ): TFGToolState

    /**
     * Internal release: close the 2FG gripper to release an object held from the inside.
     *
     * This method represents the **internal release** operation of the OnRobot **2FG7** and **2FG14** grippers.
     * The fingers move inward to disengage from the inner walls of a cavity, tube, or hole.
     * Although internally similar to [gripInt], it is provided as a separate command for
     * clearer semantics and a better developer experience.
     *
     * @param width target finger distance (mm).
     * @param force release force (N).
     * @param speed release speed (mm/s).
     * @param cmdTimeout maximum time (in milliseconds) to wait for the command to complete.
     * @param popupMsg enable/disable popup messages on Polyscope.
     * @param onChange ⚠️ **Experimental:** optional callback that is invoked whenever the [TFGToolState]
     *   changes during execution. This API is experimental and may change or be removed in future versions.
     * @param onFinished optional callback that is invoked once when the [TFGToolState] is final.
     *   The state is final when `runningState` assumes the following value:
     *   [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END],
     *   [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT],
     *   [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
     * @return [TFGToolState] continuously updated execution state of the URScript.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.onRobotTFGReleaseInt
     */
    fun releaseInt(
        width: Double,
        force: Int,
        speed: Int,
        cmdTimeout: Long? = null,
        popupMsg: Boolean = Default.POPUP_MSG,
        onChange: ((TFGToolState) -> Unit)? = null,
        onFinished: ((TFGToolState) -> Unit)? = null
    ): TFGToolState
}
