package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_state.RobotState
import java.nio.ByteBuffer

internal interface StateDecoder {
    fun decode(bb: ByteBuffer): RobotState?
}