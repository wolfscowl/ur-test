package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.RobotModeData
import com.wolfscowl.ur_client.model.robot_state.RobotState
import com.wolfscowl.ur_client.model.robot_state.mode.ControlMode
import com.wolfscowl.ur_client.model.robot_state.mode.RobotMode
import java.nio.ByteBuffer

internal object RobotModeDataDecoder: StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState = RobotModeData(
        timestamp = bb.long.toULong(),
        realRobotConnected = bb.get().toInt() == 1,
        realRobotEnabled = bb.get().toInt() == 1,
        powerOnRobot = bb.get().toInt() == 1,
        emergencyStopped = bb.get().toInt() == 1,
        securityStopped = bb.get().toInt() == 1,
        programRunning = bb.get().toInt() == 1,
        programPaused = bb.get().toInt() == 1,
        robotMode =  RobotMode.fromCode( bb.get().toInt() and 0xFF ),
        controlMode = ControlMode.fromCode( bb.get().toInt() and 0xFF ),
        speedFraction = bb.double,
        speedScaling = bb.double,
        speedFractionLimit = bb.double,
        reserved = bb.get().toInt() and 0xFF,
    )
}