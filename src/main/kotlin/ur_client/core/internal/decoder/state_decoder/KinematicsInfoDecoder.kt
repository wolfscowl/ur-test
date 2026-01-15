package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.KinematicsInfo
import com.wolfscowl.ur_client.model.robot_state.RobotState
import java.nio.ByteBuffer

internal object KinematicsInfoDecoder: StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState? {
        // - Primary Interface Bugfix -
        // Primary interface sends sequential KinematicsInfo.
        // If the KinematicsInfo has not changed, only 4 bytes are sent.
        if (bb.limit() != 220) {
            return null
        }
        return KinematicsInfo(
            jointKinematicsInfo = (0..5).map { jointIndex ->
                KinematicsInfo.JointKinematicsInfo(
                    checksum = bb.getInt(jointIndex * 4).toLong() and 0xFFFFFFFFL,  // loop1 -> offset = jointIndex * 4 bytes (1x long)
                    dhTheta = bb.getDouble(jointIndex * 8 + 24),                    // loop2 -> offset = jointIndex * 8 bytes (1x double) + 24 bytes (6x long)
                    dhA = bb.getDouble(jointIndex * 8 + 72),                        // loop3 -> offset = jointIndex * 8 bytes (1x double) + 48 bytes (6x long + 6x double)
                    dhD = bb.getDouble(jointIndex * 8 + 120),                       // loop4 -> offset = jointIndex * 8 bytes (1x double) + 120 bytes (6x long + 12x double)
                    dhAlpha = bb.getDouble(jointIndex * 8 + 168)                    // loop5 -> offset = jointIndex * 8 bytes (1x double) + 168 bytes (6x long + 18x double)
                )
            },
            calibration_status = bb.getInt(216).toLong() and 0xFFFFFFFFL            // offset = 216 bytes (6x long + 24x double)
        )
    }
}