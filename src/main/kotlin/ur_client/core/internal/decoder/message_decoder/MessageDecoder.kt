package com.wolfscowl.ur_client.core.internal.decoder.message_decoder

import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import java.nio.ByteBuffer

internal interface MessageDecoder {
    fun decode(bb: ByteBuffer): RobotMessage
}