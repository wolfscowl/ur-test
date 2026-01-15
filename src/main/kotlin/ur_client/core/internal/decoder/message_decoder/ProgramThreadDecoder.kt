package com.wolfscowl.ur_client.core.internal.decoder.message_decoder

import com.wolfscowl.ur_client.model.robot_message.ProgramThreadsMessage
import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal object ProgramThreadDecoder: MessageDecoder {
    override fun decode(bb: ByteBuffer): RobotMessage = ProgramThreadsMessage(
        timestamp = bb.long.toULong(),
        source = MessageSource.fromCode(bb.get().toInt()).also { bb.get() }, // skip RobotMessageType
        programThreads = generateSequence {
            if (bb.remaining() > 0) {
                ProgramThreadsMessage.ProgramThread(
                    labelId = bb.int,
                    labelName = bb.run {
                        val byteArray = ByteArray(bb.int)
                        bb.get(byteArray)
                        String(byteArray, Charset.forName("UTF-8"))
                    },
                    threadName = bb.run {
                        val byteArray = ByteArray(bb.int)
                        bb.get(byteArray)
                        String(byteArray, Charset.forName("UTF-8"))
                    }
                )
            } else null
        }.toList()
    )
}