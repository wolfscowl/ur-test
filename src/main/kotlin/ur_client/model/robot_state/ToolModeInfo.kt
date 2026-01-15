package com.wolfscowl.ur_client.model.robot_state


// ROBOT_STATE_PACKAGE_TYPE_TOOL_MODE_INFO = 12
data class ToolModeInfo (
    val outputMode: Int,                        // uint8_t
    val digitalOutputModeOutput0: Int,          // uint8_t  ToDo - maybe rename modeDigitalOutput1
    val digitalOutputModeOutput1: Int,          // uint8_t   ToDo - maybe rename modeDigitalOutput2
): RobotState {
    override fun toString(): String {
        return buildString {
            append("-=ToolModeInfo=-\n")
            append("  outputMode = $outputMode\n")
            append("  digitalOutputModeOutput0 = $digitalOutputModeOutput0\n")
            append("  digitalOutputModeOutput1 = $digitalOutputModeOutput1")
        }
    }
}