package com.wolfscowl.ur_client.core.internal.decoder.message_decoder

import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_message.TextMessage
import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal object TextDecoder: MessageDecoder {
    override fun decode(bb: ByteBuffer): RobotMessage = TextMessage(
        timestamp = bb.long.toULong(),
        source = MessageSource.fromCode(bb.get().toInt()).also { bb.get() }, // skip RobotMessageType
        textTextMessage = bb.run {
            val byteArray = ByteArray(bb.remaining())
            bb.get(byteArray)
            String(byteArray, Charset.forName("UTF-8"))
        }
    )
}