package com.wolfscowl.ur_client.model.robot_message

import com.wolfscowl.ur_client.model.robot_message.type.MessageSource

// ROBOT_MESSAGE_TYPE_RUNTIME_EXCEPTION = 10
data class RuntimeExceptionMessage(
    override val timestamp: ULong,                      // uint64_t
    override val source: MessageSource,                 // char
    val scriptLineNumber: Int,                          // int
    val scriptColumnNumber: Int,                        // int
    val runtimeExceptionTextMessage: String             // char array
) : RobotMessage {
    override fun toString(): String {
        return buildString {
            append("-=RuntimeExceptionMessage=-\n")
            append("  timestamp = $timestamp\n")
            append("  source = $source\n")
            append("  scriptLineNumber = $scriptLineNumber\n")
            append("  scriptColumnNumber = $scriptColumnNumber\n")
            append("  runtimeExceptionTextMessage = $runtimeExceptionTextMessage")
        }
    }
}