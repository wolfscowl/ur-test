package com.wolfscowl.ur_client.model.robot_message

import com.wolfscowl.ur_client.model.robot_message.type.MessageSource

// ROBOT_MESSAGE_TYPE_VERSION = 3
data class VersionMessage(
    override val timestamp: ULong,                      // uint64_t
    override val source: MessageSource,                 // char
    val projectName: String,                            // charArray
    val majorVersion: Int,                              // unsigned char
    val minorVersion: Int,                              // unsigned char
    val bugfixVersion: Int,                             // int
    val buildNumber: Int,                               // int
    val buildDate: String                               // charArray
) : RobotMessage {
    override fun toString(): String {
        return buildString {
            append("-=VersionMessage=-\n")
            append("  timestamp = $timestamp\n")
            append("  source = $source\n")
            append("  projectName = $projectName\n")
            append("  majorVersion = $majorVersion\n")
            append("  minorVersion = $minorVersion\n")
            append("  bugfixVersion = $bugfixVersion\n")
            append("  buildNumber = $buildNumber\n")
            append("  buildDate = $buildDate")
        }
    }
}
