package com.wolfscowl.ur_client.model.robot_state.mode

enum class ControlMode(val code: Int, val description: String) {
    CONTROL_MODE_POSITION(0, "Position Control"),
    CONTROL_MODE_TEACH(1, "Teach Mode"),
    CONTROL_MODE_FORCE(2, "Force Control"),
    CONTROL_MODE_TORQUE(3, "Torque Control"),
    CONTROL_MODE_UNDEFINED(-9999, "Undefined control mode");

    companion object {
        fun fromCode(code: Int): ControlMode = ControlMode.entries.find { it.code == code } ?: CONTROL_MODE_UNDEFINED
    }
}