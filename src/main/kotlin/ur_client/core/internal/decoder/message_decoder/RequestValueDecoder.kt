package com.wolfscowl.ur_client.core.internal.decoder.message_decoder

import com.wolfscowl.ur_client.model.robot_message.RequestValueMessage
import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import com.wolfscowl.ur_client.model.robot_message.type.RequestType
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal object RequestValueDecoder: MessageDecoder {
    override fun decode(bb: ByteBuffer): RobotMessage = RequestValueMessage(
        timestamp = bb.long.toULong(),
        source = MessageSource.fromCode(bb.get().toInt()).also { bb.get() }, // skip RobotMessageType
        requestID = bb.int.toLong() and 0xFFFFFFFFL,
        requestedType = RequestType.fromCode(bb.int.toLong() and 0xFFFFFFFFL ),
        requestTextMessage = bb.run {
            val byteArray = ByteArray(bb.remaining())
            bb.get(byteArray)
            String(byteArray, Charset.forName("UTF-8"))
        }
    )
}