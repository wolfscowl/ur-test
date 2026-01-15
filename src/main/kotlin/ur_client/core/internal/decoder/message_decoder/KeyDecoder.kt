package com.wolfscowl.ur_client.core.internal.decoder.message_decoder

import com.wolfscowl.ur_client.model.robot_message.KeyMessage
import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal object KeyDecoder: MessageDecoder {
    override fun decode(bb: ByteBuffer): RobotMessage = KeyMessage(
        timestamp = bb.long.toULong(),
        source = MessageSource.fromCode(bb.get().toInt()).also { bb.get() }, // skip RobotMessageType
        robotMessageCode = bb.int,
        robotMessageArgument = bb.int,
        robotMessageTitle = bb.run {
            val byteArray = ByteArray(bb.get().toInt() and 0xFF)
            bb.get(byteArray)
            String(byteArray, Charset.forName("UTF-8"))
        },
        keyTextMessage = bb.run {
            val byteArray = ByteArray(bb.remaining())
            bb.get(byteArray)
            String(byteArray, Charset.forName("UTF-8"))
        }
    )
}