package com.wolfscowl.ur_client.model.robot_state

import com.wolfscowl.ur_client.core.internal.util.Util.formatString


// ROBOT_STATE_PACKAGE_TYPE_KINEMATICS_INFO = 5
data class KinematicsInfo (
    // ToDo make extra values (base,shoulder,elbow, wrist1, wrist2, wrist3) for jointKinematicsInfo
    val jointKinematicsInfo: List<JointKinematicsInfo>,
    val calibration_status: Long                            // uint32_t
): RobotState {
    override fun toString(): String {
        return buildString {
            append("-=KinematicsInfo=-\n")
            append("  calibration_status = $calibration_status\n")
            append("  jointKinematicsInfo = ")
            jointKinematicsInfo.forEach { jointKinematics ->
                append("\n")
                append(jointKinematics.toString().formatString(false, 4))
            }
        }
    }

    data class JointKinematicsInfo(
        val checksum: Long,                 // uint32_t
        val dhTheta: Double,                // double
        val dhA: Double,                    // double
        val dhD: Double,                    // double
        val dhAlpha: Double,                // double
    ) {
        override fun toString(): String {
            return buildString {
                append("-=JointKinematicsInfo=-\n")
                append("  checksum = $checksum\n")
                append("  dhTheta = $dhTheta\n")
                append("  dhA = $dhA\n")
                append("  dhD = $dhD\n")
                append("  dhAlpha = $dhAlpha")
            }
        }
    }
}
