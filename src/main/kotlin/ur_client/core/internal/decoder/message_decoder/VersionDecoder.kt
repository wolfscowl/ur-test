package com.wolfscowl.ur_client.core.internal.decoder.message_decoder

import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_message.VersionMessage
import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal object VersionDecoder: MessageDecoder {
    override fun decode(bb: ByteBuffer): RobotMessage = VersionMessage(
        timestamp = bb.long.toULong(),
        source = MessageSource.fromCode(bb.get().toInt()).also { bb.get() }, // skip RobotMessageType
        projectName = bb.run {
            val byteArray = ByteArray(bb.get().toInt())
            bb.get(byteArray)
            String(byteArray, Charset.forName("UTF-8"))
        },
        majorVersion = bb.get().toInt() and 0xFF,
        minorVersion = bb.get().toInt() and 0xFF,
        bugfixVersion = bb.int,
        buildNumber = bb.int,
        buildDate = bb.run {
            val byteArray = ByteArray(bb.remaining())
            bb.get(byteArray)
            String(byteArray, Charset.forName("UTF-8"))
        },
    )
}