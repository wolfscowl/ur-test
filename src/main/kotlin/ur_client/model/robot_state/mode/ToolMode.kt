package com.wolfscowl.ur_client.model.robot_state.mode

enum class ToolMode(val code: Int, val description: String) {
    TOOL_MODE_RESET(235, "Reset"),
    TOOL_MODE_SHUTTING_DOWN(236, "Shutting Down"),
    TOOL_MODE_POWER_OFF(239, "Power Off"),
    TOOL_MODE_NOT_RESPONDING(245, "Not Responding"),
    TOOL_MODE_BOOTING(247, "Booting"),
    TOOL_MODE_BOOTLOADER(249, "Bootloader"),
    TOOL_MODE_FAULT(252, "Fault"),
    TOOL_MODE_RUNNING(253, "Running"),
    TOOL_MODE_IDLE(255, "Idle"),
    TOOL_MODE_UNDEFINED(-9999, "Undefined");

    companion object {
        fun fromCode(code: Int): ToolMode = ToolMode.entries.find { it.code == code } ?: TOOL_MODE_UNDEFINED
    }
}