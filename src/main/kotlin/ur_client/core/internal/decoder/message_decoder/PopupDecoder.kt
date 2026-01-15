package com.wolfscowl.ur_client.core.internal.decoder.message_decoder

import com.wolfscowl.ur_client.model.robot_message.PopupMessage
import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import com.wolfscowl.ur_client.model.robot_message.type.RequestType
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal object PopupDecoder: MessageDecoder {
    override fun decode(bb: ByteBuffer): RobotMessage = PopupMessage(
        timestamp = bb.long.toULong(),
        source = MessageSource.fromCode(bb.get().toInt()).also { bb.get() }, // skip RobotMessageType
        requestID = bb.int.toLong() and 0xFFFFFFFFL,
        requestedType = RequestType.fromCode(bb.int.toLong() and 0xFFFFFFFFL ),
        warning = bb.get().toInt() == 1,
        error = bb.get().toInt() == 1,
        blocking = bb.get().toInt() == 1,
        popupMessageTitle = bb.run {
            val byteArray = ByteArray(bb.get().toInt() and 0xFF)
            bb.get(byteArray)
            String(byteArray, Charset.forName("UTF-8"))
        },
        popupTextMessage = bb.run {
            val byteArray = ByteArray(bb.remaining())
            bb.get(byteArray)
            String(byteArray, Charset.forName("UTF-8"))
        }
    )
}