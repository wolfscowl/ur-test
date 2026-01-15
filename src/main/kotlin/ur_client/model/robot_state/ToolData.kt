package com.wolfscowl.ur_client.model.robot_state

import com.wolfscowl.ur_client.model.robot_state.mode.ToolMode

// ROBOT_STATE_PACKAGE_TYPE_TOOL_DATA=2
data class ToolData(
    val analogInputRange2: Int,     // unsigned char
    val analogInputRange3: Int,     // unsigned char
    val analogInput2: Double,       // double
    val analogInput3: Double,       // double
    val toolVoltage48v: Double,     // float
    val toolOutputVoltage: Int,     // unsigned char
    val toolCurrent: Double,        // float
    val toolTemperature: Double,    // float
    val toolMode: ToolMode          // uint8_t
): RobotState {
    override fun toString(): String {
        return buildString {
            append("-=ToolData=-\n")
            append("  analogInputRange2 = $analogInputRange2\n")
            append("  analogInputRange3 = $analogInputRange3\n")
            append("  analogInput2 = $analogInput2\n")
            append("  analogInput3 = $analogInput3\n")
            append("  toolVoltage48v = $toolVoltage48v\n")
            append("  toolOutputVoltage = $toolOutputVoltage\n")
            append("  toolCurrent = $toolCurrent\n")
            append("  toolTemperature = $toolTemperature\n")
            append("  toolMode = $toolMode")
        }
    }
}