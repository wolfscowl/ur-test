package com.wolfscowl.ur_client.model.robot_message


import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.model.robot_message.type.MessageSource

// ROBOT_MESSAGE_TYPE_PROGRAM_LABEL_THREADS = 14
data class ProgramThreadsMessage(
    override val timestamp: ULong,                      // uint64_t
    override val source: MessageSource,                 // char
    val programThreads: List<ProgramThread>
): RobotMessage {
    override fun toString(): String {
        return buildString {
            append("-=ProgramThreadsMessage=-\n")
            append("  timestamp = $timestamp\n")
            append("  source = $source\n")
            append("  programThreads = ")
            programThreads.forEach { thread ->
                append("\n")
                append(thread.toString().formatString(false,4))
            }
        }
    }
    data class ProgramThread(
        val labelId: Int,                                       // int (line number)
        val labelName: String,                                  // charArray
        val threadName: String                                  // charArray
    ) {
        override fun toString(): String {
            return buildString {
                append("-=ProgramThread=-\n")
                append("  labelId =  $labelId\n")
                append("  labelName = $labelName\n")
                append("  threadName = $threadName")
            }
        }
    }

}