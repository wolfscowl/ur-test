package com.wolfscowl.ur_client.model.robot_state.mode

enum class RobotMode(val code: Int, val description: String) {
    ROBOT_MODE_NO_CONTROLLER(-1, "No Controller"),
    ROBOT_MODE_DISCONNECTED(0, "Disconnected"),
    ROBOT_MODE_CONFIRM_SAFETY(1, "Confirm Safety"),
    ROBOT_MODE_BOOTING(2, "Booting"),
    ROBOT_MODE_POWER_OFF(3, "Power Off"),
    ROBOT_MODE_POWER_ON(4, "Power On"),
    ROBOT_MODE_IDLE(5, "Idle"),
    ROBOT_MODE_BACKDRIVE(6, "Backdrive"),
    ROBOT_MODE_RUNNING(7, "Running"),
    ROBOT_MODE_UPDATING_FIRMWARE(8, "Updating Firmware"),
    ROBOT_MODE_UNDEFINED(-9999, "Undefined");

    companion object {
        fun fromCode(code: Int): RobotMode = RobotMode.entries.find { it.code == code } ?: ROBOT_MODE_UNDEFINED
    }
}