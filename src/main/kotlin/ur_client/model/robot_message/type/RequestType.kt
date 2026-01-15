package com.wolfscowl.ur_client.model.robot_message.type


enum class RequestType(val code: Long, val description: String) {
    REQUEST_VALUE_TYPE_BOOLEAN(0, "Boolean value type"),
    REQUEST_VALUE_TYPE_INTEGER(1, "Integer value type"),
    REQUEST_VALUE_TYPE_FLOAT(2, "Float value type"),
    REQUEST_VALUE_TYPE_STRING(3, "String value type"),
    REQUEST_VALUE_TYPE_POSE(4, "Pose value type"),
    REQUEST_VALUE_TYPE_JOINTVECTOR(5, "Joint vector value type"),
    REQUEST_VALUE_TYPE_WAYPOINT(6, "Waypoint value type (UNUSED)"),
    REQUEST_VALUE_TYPE_EXPRESSION(7, "Expression value type (UNUSED)"),
    REQUEST_VALUE_TYPE_NONE(8, "None value type"),
    REQUEST_VALUE_UNDEFINED(-9999, "Undefined value type");

    companion object {
        fun fromCode(code: Long): RequestType = RequestType.entries.find { it.code == code } ?: REQUEST_VALUE_UNDEFINED
    }
}