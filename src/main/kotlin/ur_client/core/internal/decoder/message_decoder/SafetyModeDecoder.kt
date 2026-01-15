package com.wolfscowl.ur_client.core.internal.decoder.message_decoder

import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_message.SafetyModeMessage
import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import com.wolfscowl.ur_client.model.robot_message.type.SafetyModeType
import java.nio.ByteBuffer

internal object SafetyModeDecoder: MessageDecoder {
    override fun decode(bb: ByteBuffer): RobotMessage = SafetyModeMessage(
        timestamp = bb.long.toULong(),
        source = MessageSource.fromCode(bb.get().toInt()).also { bb.get() }, // skip RobotMessageType
        robotMessageCode = bb.int,
        robotMessageArgument = bb.int,
        safetyModeType = SafetyModeType.fromCode(bb.get().toInt() and 0xFF),
        reportDataType = bb.int.toLong() and 0xFFFFFFFFL,
        reportData = bb.int.toLong() and 0xFFFFFFFFL
    )
}