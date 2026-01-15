package com.wolfscowl.ur_client.model.robot_message

import com.wolfscowl.ur_client.model.robot_message.type.MessageSource
import com.wolfscowl.ur_client.model.robot_message.type.RequestType

// RobotMessageType = ROBOT_MESSAGE_TYPE_POPUP = 2
data class PopupMessage(
    override val timestamp: ULong,                      // uint64_t
    override val source: MessageSource,                 // char
    val requestID: Long,                                // unsigned int
    val requestedType: RequestType,                     // unsigned int
    val warning: Boolean,                               // bool
    val error: Boolean,                                 // bool
    val blocking: Boolean,                              // bool
    val popupMessageTitle: String,                      // int
    val popupTextMessage: String,                       // int
) : RobotMessage {
    override fun toString(): String {
        return buildString {
            append("-=PopupMessage=-\n")
            append("  timestamp = $timestamp\n")
            append("  source = $source\n")
            append("  requestID = $requestID\n")
            append("  requestedType = $requestedType\n")
            append("  warning = $warning\n")
            append("  error = $error\n")
            append("  blocking = $blocking\n")
            append("  popupMessageTitle = $popupMessageTitle\n")
            append("  popupTextMessage = $popupTextMessage")
        }
    }
}
