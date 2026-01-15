package com.wolfscowl.ur_client.model.robot_message

import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import com.wolfscowl.ur_client.model.robot_message.type.ReportLevel

// ROBOT_MESSAGE_TYPE_ERROR_CODE = 6
data class RobotCommMessage(
    override val timestamp: ULong,                      // uint64_t
    override val source: MessageSource,                 // char
    val robotMessageCode: Int,                          // int
    val robotMessageArgument: Int,                      // int
    val robotMessageReportLevel: ReportLevel,           // int
    val robotMessageDataType: Long,                     // uint32_t
    val robotMessageData: Long,                         // uint32_t
    val robotCommTextMessage: String                    // charArray
): RobotMessage {

    override fun toString(): String {
        return buildString {
            append("-=RobotCommMessage=-\n")
            append("  timestamp = $timestamp\n")
            append("  source = $source\n")
            append("  robotMessageCode = $robotMessageCode\n")
            append("  robotMessageArgument = $robotMessageArgument\n")
            append("  robotMessageReportLevel = $robotMessageReportLevel\n")
            append("  robotMessageDataType = $robotMessageDataType\n")
            append("  robotMessageData = $robotMessageData\n")
            append("  robotCommTextMessage = $robotCommTextMessage")
        }
    }
}