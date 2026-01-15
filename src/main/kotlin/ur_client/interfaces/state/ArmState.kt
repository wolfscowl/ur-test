package com.wolfscowl.ur_client.interfaces.state

import com.wolfscowl.ur_client.core.internal.state.MutableArmState
import com.wolfscowl.ur_client.model.element.Inertia
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.Vec3

interface ArmState: URScriptState {
    val jointPosition: JointPosition
    val tcpPose: Pose
    val tcpOffset: Pose
    val payload: Float
    val payloadCog: Vec3
    val payloadInertia: Inertia

    override fun copy() : ArmState

    fun toFormattedString(
        jointsInDegree: Boolean = false,
        tcpPoseInDegree: Boolean = false,
        tcpPoseInMillimeter: Boolean = false,
        tcpOffsetInDegree: Boolean = false,
        tcpOffsetInMillimeter: Boolean = false,
        roundDecimals: Boolean = false,
    ): String
}