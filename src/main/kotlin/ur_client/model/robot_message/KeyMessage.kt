package com.wolfscowl.ur_client.model.robot_message


import com.wolfscowl.ur_client.model.robot_message.type.MessageSource


// ROBOT_MESSAGE_TYPE_KEY = 7
data class KeyMessage(
    override val timestamp: ULong,                      // uint64_t
    override val source: MessageSource,                 // char
    val robotMessageCode: Int,                          // int
    val robotMessageArgument: Int,                      // int
    val robotMessageTitle: String,                      // charArray
    val keyTextMessage: String,                         // charArray
): RobotMessage {

    override fun toString(): String {
        return buildString {
            append("-=RobotKeyMessage=-\n")
            append("  timestamp = $timestamp\n")
            append("  source = $source\n")
            append("  robotMessageCode = $robotMessageCode\n")
            append("  robotMessageArgument = $robotMessageArgument\n")
            append("  robotMessageTitle = $robotMessageTitle\n")
            append("  keyTextMessage = $keyTextMessage")
        }
    }
}