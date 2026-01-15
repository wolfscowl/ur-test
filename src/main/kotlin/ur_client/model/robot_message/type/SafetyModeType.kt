package com.wolfscowl.ur_client.model.robot_message.type

enum class SafetyModeType(val code: Int, val description: String) {
    SAFETY_MODE_UNDEFINED(11, "General safety mode"),
    SAFETY_MODE_VALIDATE_JOINT_ID(10, "Safety mode validation joint id"),
    SAFETY_MODE_FAULT(9, "Fault safety mode"),
    SAFETY_MODE_VIOLATION(8, "Violation safety mode"),
    SAFETY_MODE_ROBOT_EMERGENCY_STOP(7, "Robot emergency stop (EA + EB + SBUS -> Euromap67) Physical e-stop interface input activated"),
    SAFETY_MODE_SYSTEM_EMERGENCY_STOP(6, "System emergency stop (EA + EB + SBUS -> Screen) Physical e-stop interface input activated"),
    SAFETY_MODE_SAFEGUARD_STOP(5, "Safeguard stop (SI0 + SI1 + SBUS) Physical stop interface input"),
    SAFETY_MODE_RECOVERY(4, "Safety recovery mode"),
    SAFETY_MODE_PROTECTIVE_STOP(3, "Protective stop safety mode"),
    SAFETY_MODE_REDUCED(2, "Reduced safety mode"),
    SAFETY_MODE_NORMAL(1, "Normal safety mode");

    companion object {
        fun fromCode(code: Int): SafetyModeType = SafetyModeType.entries.find { it.code == code } ?: SAFETY_MODE_UNDEFINED
    }
}