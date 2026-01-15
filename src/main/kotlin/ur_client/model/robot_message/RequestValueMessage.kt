package com.wolfscowl.ur_client.model.robot_message

import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import com.wolfscowl.ur_client.model.robot_message.type.RequestType

// ROBOT_MESSAGE_TYPE_REQUEST_VALUE = 9
data class RequestValueMessage(
    override val timestamp: ULong,                      // uint64_t
    override val source: MessageSource,                 // char
    val requestID: Long,                                // unsigned int
    val requestedType: RequestType,                     // unsigned int
    val requestTextMessage: String                      // char array
) : RobotMessage {
    override fun toString(): String {
        return buildString {
            append("-=RequestValueMessage=-\n")
            append("  timestamp = $timestamp\n")
            append("  source = $source\n")
            append("  requestID = $requestID\n")
            append("  requestedType = $requestedType\n")
            append("  requestTextMessage = $requestTextMessage")
        }
    }
}