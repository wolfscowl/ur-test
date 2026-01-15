package com.wolfscowl.ur_client.model.robot_state.mode

enum class JointMode(val code: Int, val description: String) {
    JOINT_MODE_RESET(235, "Reset"),
    JOINT_MODE_SHUTTING_DOWN(236, "Shutting Down"),
    JOINT_MODE_BACKDRIVE(238, "Backdrive"),
    JOINT_MODE_POWER_OFF(239, "Power Off"),
    JOINT_MODE_READY_FOR_POWER_OFF(240, "Ready for Power Off"),  // from version 5.1
    JOINT_MODE_NOT_RESPONDING(245, "Not Responding"),
    JOINT_MODE_MOTOR_INITIALISATION(246, "Motor Initialization"),
    JOINT_MODE_BOOTING(247, "Booting"),
    JOINT_MODE_VIOLATION(51, "Violation"),
    JOINT_MODE_FAULT(252, "Fault"),
    JOINT_MODE_RUNNING(253, "Running"),
    JOINT_MODE_IDLE(255, "Idle"),
    JOINT_MODE_UNDEFINED(-9999, "Undefined");

    companion object {
        fun fromCode(code: Int): JointMode = JointMode.entries.find { it.code == code } ?: JOINT_MODE_UNDEFINED
    }
}