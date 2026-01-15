package com.wolfscowl.ur_client.core.internal.decoder.message_decoder

import com.wolfscowl.ur_client.model.robot_message.RobotCommMessage
import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import com.wolfscowl.ur_client.model.robot_message.type.ReportLevel
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal object RobotCommDecoder: MessageDecoder {
    override fun decode(bb: ByteBuffer): RobotMessage = RobotCommMessage(
        timestamp = bb.long.toULong(),
        source = MessageSource.fromCode(bb.get().toInt()).also { bb.get() }, // skip RobotMessageType
        robotMessageCode = bb.int,
        robotMessageArgument = bb.int,
        robotMessageReportLevel = ReportLevel.fromCode(bb.int),
        robotMessageDataType = bb.int.toLong() and 0xFFFFFFFFL,
        robotMessageData = bb.int.toLong() and 0xFFFFFFFFL,
        robotCommTextMessage = bb.run {
            val byteArray = ByteArray(bb.remaining())
            bb.get(byteArray)
            String(byteArray, Charset.forName("UTF-8"))
        }
    )
}