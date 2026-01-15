package com.wolfscowl.ur_client.core.internal.decoder.message_decoder

import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_message.RuntimeExceptionMessage
import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal object RuntimeExceptionDecoder: MessageDecoder {
    override fun decode(bb: ByteBuffer): RobotMessage = RuntimeExceptionMessage(
        timestamp = bb.long.toULong(),
        source = MessageSource.fromCode(bb.get().toInt()).also { bb.get() }, // skip RobotMessageType
        scriptLineNumber = bb.int,
        scriptColumnNumber = bb.int,
        runtimeExceptionTextMessage = bb.run {
            val byteArray = ByteArray(bb.remaining())
            bb.get(byteArray)
            String(byteArray, Charset.forName("UTF-8")).filterNot{ it.isISOControl() }  // remove control character
        }
    )
}