package com.wolfscowl.ur_client.model.robot_state.mode

enum class SafetyMode(val code: Int, val description: String) {
    SAFETY_MODE_UNDEFINED_(-1, "Undefined"),
    SAFETY_MODE(11, "Safety Mode"),
    SAFETY_MODE_VALIDATE_JOINT_ID(10, "Validate Joint ID"),
    SAFETY_MODE_FAULT(9, "Fault"),
    SAFETY_MODE_VIOLATION(8, "Violation"),
    SAFETY_MODE_ROBOT_EMERGENCY_STOP(7, "Robot Emergency Stop (EA + EB + SBUS -> Euromap67) Physical e-stop interface input activated"),
    SAFETY_MODE_SYSTEM_EMERGENCY_STOP(6, "System Emergency Stop (EA + EB + SBUS -> Screen) Physical e-stop interface input activated"),
    SAFETY_MODE_SAFEGUARD_STOP(5, "Safeguard Stop (SI0 + SI1 + SBUS) Physical s-stop interface input activated"),
    SAFETY_MODE_RECOVERY(4, "Recovery"),
    SAFETY_MODE_PROTECTIVE_STOP(3, "Protective Stop"),
    SAFETY_MODE_REDUCED(2, "Reduced"),
    SAFETY_MODE_NORMAL(1, "Normal"),
    SAFETY_MODE_UNDEFINED(-9999, "Undefined");

    companion object {
        fun fromCode(code: Int): SafetyMode = SafetyMode.entries.find { it.code == code } ?: SAFETY_MODE_UNDEFINED
    }
}