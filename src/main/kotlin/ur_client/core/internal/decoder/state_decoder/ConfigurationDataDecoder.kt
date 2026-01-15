package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.ConfigurationData
import com.wolfscowl.ur_client.model.robot_state.RobotState
import java.nio.ByteBuffer

internal object ConfigurationDataDecoder:
    com.wolfscowl.ur_client.core.internal.decoder.state_decoder.StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState = ConfigurationData(
        jointConfigurationData = (0..5).map { jointIndex ->
            ConfigurationData.JointConfigurationData(
                jointMinLimit = bb.getDouble(jointIndex * 16),                  // loop 1 -> offset = jointIndex * 16 bytes (2x double)
                jointMaxLimitt = bb.getDouble(jointIndex * 16 + 8),             // loop 1 -> offset = jointIndex * 16 bytes (2x double) + 8 bytes (1x double)
                jointMaxSpeed = bb.getDouble(jointIndex * 16 + 96),             // loop 2 -> offset = jointIndex * 16 bytes (2x double) + 96 bytes (12x double)
                jointMaxAcceleration = bb.getDouble(jointIndex * 16 + 104),     // loop 2 -> offset = jointIndex * 16 bytes (2x double) + 104 bytes (13x double)
                DHa = bb.getDouble(jointIndex * 8 + 192 + 40),                  // loop 3 -> offset = jointIndex * 8 bytes (1x double) + 192 bytes (24x double) + 40 bytes (5x double)
                Dhd = bb.getDouble(jointIndex * 8 + 240 + 40),                  // loop 4 -> offset = jointIndex * 8 bytes (1x double) + 240 bytes (30x double) + 40 bytes (5x Double)
                DHalpha = bb.getDouble(jointIndex * 8 + 288 + 40),              // loop 5 -> offset = jointIndex * 8 bytes (1x double) + 288 bytes (36x double) + 40 bytes (5x Double)
                DHtheta = bb.getDouble(jointIndex * 8 + 336 + 40)               // loop 6 -> offset = jointIndex * 8 bytes (1x double) + 336 bytes (42x double) + 40 bytes (5x Double)
            )
        },
        vJointDefault = bb.getDouble(192),                                      // offset = 192 bytes (24x double)
        aJointDefault = bb.getDouble(200),                                      // offset = 200 bytes (25x double)
        vToolDefault = bb.getDouble(208),                                       // offset = 208 bytes (26x double)
        aToolDefault = bb.getDouble(216),                                       // offset = 216 bytes (27x double)
        eqRadius = bb.getDouble(224),                                           // offset = 224 bytes (28x double)
        masterboardVersion = bb.getInt(424),                                    // offset = 424 bytes (53x double)
        controllerBoxType = bb.getInt(428),                                     // offset = 428 bytes (53x double + 1x int)
        robotType = bb.getInt(432),                                             // offset = 432 bytes (53x double + 2x int)
        robotSubType = bb.getInt(436),                                          // offset = 436 bytes (53x double + 3x int)
    )
}