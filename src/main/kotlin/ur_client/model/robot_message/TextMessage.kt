package com.wolfscowl.ur_client.model.robot_message

import com.wolfscowl.ur_client.model.robot_message.type.MessageSource

// ROBOT_MESSAGE_TYPE_TEXT = 0
data class TextMessage(
    override val timestamp: ULong,                      // uint64_t
    override val source: MessageSource,                 // char
    val textTextMessage: String                         // char array
) : RobotMessage {
    override fun toString(): String {
        return buildString {
            append("-=TextMessage=-\n")
            append("  timestamp = $timestamp\n")
            append("  source = $source\n")
            append("  textTextMessage = $textTextMessage")
        }
    }
}