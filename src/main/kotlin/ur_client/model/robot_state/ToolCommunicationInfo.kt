package com.wolfscowl.ur_client.model.robot_state

// ROBOT_STATE_PACKAGE_TYPE_TOOL_COMM_INFO=11
data class ToolCommunicationInfo (
    val toolCommunicationIsEnabled: Boolean,    // bool
    val baudRate: Int,                          // int32_t
    val parity: Int,                            // int32_t
    val stopBits: Int,                          // int32_t
    val rxIdleChars: Double,                    // float
    val txIdleChars: Double,                    // float
): RobotState {
    override fun toString(): String {
        return buildString {
            append("-=ToolCommunicationInfo=-\n")
            append("  toolCommunicationIsEnabled = $toolCommunicationIsEnabled\n")
            append("  baudRate = $baudRate\n")
            append("  parity = $parity\n")
            append("  stopBits = $stopBits\n")
            append("  rxIdleChars = $rxIdleChars\n")
            append("  txIdleChars = $txIdleChars")
        }
    }
}