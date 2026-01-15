package com.wolfscowl.ur_client.model.robot_message

import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import com.wolfscowl.ur_client.model.robot_message.type.SafetyModeType

// ROBOT_MESSAGE_TYPE_SAFETY_MODE = 5
class SafetyModeMessage(
    override val timestamp: ULong,                      // uint64_t
    override val source: MessageSource,                 // char
    val robotMessageCode: Int,                          // int
    val robotMessageArgument: Int,                      // int
    val safetyModeType: SafetyModeType,                 // unsigned char
    val reportDataType: Long,                           // uint32_t
    val reportData: Long,                               // uint32_t
) : RobotMessage {
    override fun toString(): String {
        return buildString {
            append("-=SafetyModeMessage=-\n")
            append("  timestamp = $timestamp\n")
            append("  source = $source\n")
            append("  robotMessageCode = $robotMessageCode\n")
            append("  robotMessageArgument = $robotMessageArgument\n")
            append("  safetyModeType = $safetyModeType\n")
            append("  reportDataType = $reportDataType\n")
            append("  reportData = $reportData")
        }
    }
}