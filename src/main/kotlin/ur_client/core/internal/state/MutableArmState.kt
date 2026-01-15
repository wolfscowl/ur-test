package com.wolfscowl.ur_client.core.internal.state

import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.interfaces.state.ArmState
import com.wolfscowl.ur_client.model.element.Inertia
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.Vec3

internal class MutableArmState(
    scriptName: String = ""
): MutableURScriptState(scriptName), ArmState {
    @Volatile
    override var jointPosition: JointPosition = JointPosition()
    @Volatile
    override var tcpPose: Pose = Pose()
    @Volatile
    override var tcpOffset: Pose = Pose()
    @Volatile
    override var payload: Float = 0.0f
    @Volatile
    override var payloadCog: Vec3 = Vec3()
    @Volatile
    override var payloadInertia: Inertia = Inertia()

    override fun toString(): String {
        return buildString {
            append("-=RGArmState=-\n")
            append(super.toString().formatString() + "\n")
            append("  jointPosition = \n")
            append(jointPosition.toString().formatString(true,2) + "\n")
            append("  tcpPose = \n")
            append(tcpPose.toString().formatString(true,2) + "\n")
            append("  tcpOffset = \n")
            append(tcpOffset.toString().formatString(true,2) + "\n")
            append("  Payload = $payload\n")
            append("  PayloadCog = \n")
            append(payloadCog.toString().formatString(true,2) + "\n")
            append("  PayloadInertia = \n")
            append(payloadInertia.toString().formatString(true,2))
        }
    }


    override fun toFormattedString(
        jointsInDegree: Boolean,
        tcpPoseInDegree: Boolean,
        tcpPoseInMillimeter: Boolean,
        tcpOffsetInDegree: Boolean,
        tcpOffsetInMillimeter: Boolean,
        roundDecimals: Boolean,
    ): String {
        val posePosDecimal = if (roundDecimals) if (tcpPoseInMillimeter) 2 else 5 else -1
        val poseRotDecimal = if (roundDecimals) if (tcpPoseInDegree) 2 else 3 else -1
        val offsetPosDecimal = if (roundDecimals) if (tcpOffsetInMillimeter) 2 else 5 else -1
        val offsetRotDecimal = if (roundDecimals) if (tcpOffsetInDegree) 2 else 3 else -1
        val jointPosDecimal = if (roundDecimals) if (jointsInDegree) 2 else 3 else -1

        return buildString {
            append("-=RGArmState=-\n")
            append(super.toString().formatString() + "\n")
            append("  jointPosition = \n")
            append(jointPosition
                .toFormattedString(
                    inDegree = jointsInDegree,
                    decimals = jointPosDecimal
                )
                .formatString(true,2) + "\n")
            append("  tcpPose = \n")
            append(tcpPose
                .toFormattedString(
                    rotInDegree = tcpPoseInDegree,
                    posInMillimeter = tcpPoseInMillimeter,
                    decimalsPos = posePosDecimal,
                    decimalsRot = poseRotDecimal,
                )
                .formatString(true,2) + "\n")
            append("  tcpOffset = \n")
            append(tcpOffset
                .toFormattedString(
                    rotInDegree = tcpOffsetInDegree,
                    posInMillimeter = tcpOffsetInMillimeter,
                    decimalsPos = offsetPosDecimal,
                    decimalsRot = offsetRotDecimal,
                )
                .formatString(true,2) + "\n")
            append("  Payload = $payload\n")
            append("  PayloadCog = \n")
            append(payloadCog.toString().formatString(true,2) + "\n")
            append("  PayloadInertia = \n")
            append(payloadInertia.toString().formatString(true,2))
        }
    }


    override fun copy() : MutableArmState =
        super.copyBaseTo(MutableArmState()).also {
            it.jointPosition = this.jointPosition.copy()
            it.tcpPose = this.tcpPose.copy()
            it.tcpPose = this.tcpPose.copy()
            it.tcpOffset = this.tcpOffset.copy()
            it.payload = this.payload
            it.payloadCog = this.payloadCog.copy()
            it.payloadInertia = this.payloadInertia.copy()
        }


    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        if (other !is MutableArmState) return false

        return  jointPosition == other.jointPosition &&
                tcpPose == other.tcpPose
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + jointPosition.hashCode() + tcpPose.hashCode()
    }

}