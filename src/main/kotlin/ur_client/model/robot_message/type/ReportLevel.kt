package com.wolfscowl.ur_client.model.robot_message.type

enum class ReportLevel(val code: Int, val description: String) {
    REPORT_LEVEL_DEBUG(0, "Debug level (INTERNAL USE ONLY)"),
    REPORT_LEVEL_INFO(1, "Informational level"),
    REPORT_LEVEL_WARNING(2, "Warning level"),
    REPORT_LEVEL_VIOLATION(3, "Violation level"),
    REPORT_LEVEL_FAULT(4, "Fault level"),
    REPORT_LEVEL_DEVL_DEBUG(128, "Developer debug level (INTERNAL USE ONLY)"),
    REPORT_LEVEL_DEVL_INFO(129, "Developer informational level"),
    REPORT_LEVEL_DEVL_WARNING(130, "Developer warning level"),
    REPORT_LEVEL_DEVL_VIOLATION(131, "Developer violation level"),
    REPORT_LEVEL_DEVL_FAULT(132, "Developer fault level"),
    REPORT_LEVEL_UNDEFINED(-9999, "Undefined level");

    companion object {
        fun fromCode(code: Int): ReportLevel = ReportLevel.entries.find { it.code == code } ?: REPORT_LEVEL_UNDEFINED
    }
}