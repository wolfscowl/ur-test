package com.wolfscowl.ur_client.model.robot_state

import com.wolfscowl.ur_client.model.robot_state.mode.ControlMode
import com.wolfscowl.ur_client.model.robot_state.mode.RobotMode

// ROBOT_STATE_PACKAGE_TYPE_ROBOT_MODE_DATA = 0
data class RobotModeData(
    val timestamp: ULong,               // uint64_t
    val realRobotConnected: Boolean,    // bool
    val realRobotEnabled: Boolean,      // bool
    val powerOnRobot: Boolean,          // bool
    val emergencyStopped: Boolean,      // bool
    val securityStopped: Boolean,       // bool
    val programRunning: Boolean,        // bool
    val programPaused: Boolean,         // bool
    val robotMode: RobotMode,           // unsigned char
    val controlMode: ControlMode,       // unsigned char
    val speedFraction: Double,          // double
    val speedScaling: Double,           // double
    val speedFractionLimit: Double,     // double
    val reserved: Int                   // unsigned char
): RobotState {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RobotModeData) return false

        return realRobotConnected == other.realRobotConnected &&
                realRobotEnabled == other.realRobotEnabled &&
                powerOnRobot == other.powerOnRobot &&
                emergencyStopped == other.emergencyStopped &&
                securityStopped == other.securityStopped &&
                programRunning == other.programRunning &&
                programPaused == other.programPaused &&
                robotMode == other.robotMode &&
                controlMode == other.controlMode &&
                speedFraction == other.speedFraction &&
                speedScaling == other.speedScaling &&
                speedFractionLimit == other.speedFractionLimit &&
                reserved == other.reserved
    }

    override fun hashCode(): Int {
        var result = realRobotConnected.hashCode()
        result = 31 * result + realRobotEnabled.hashCode()
        result = 31 * result + powerOnRobot.hashCode()
        result = 31 * result + emergencyStopped.hashCode()
        result = 31 * result + securityStopped.hashCode()
        result = 31 * result + programRunning.hashCode()
        result = 31 * result + programPaused.hashCode()
        result = 31 * result + (robotMode?.hashCode() ?: 0)
        result = 31 * result + (controlMode?.hashCode() ?: 0)
        result = 31 * result + speedFraction.hashCode()
        result = 31 * result + speedScaling.hashCode()
        result = 31 * result + speedFractionLimit.hashCode()
        result = 31 * result + reserved
        return result
    }

    override fun toString(): String {
        return buildString {
            append("-=RobotModeData=-\n")
            append("  timestamp = $timestamp\n")
            append("  realRobotConnected = $realRobotConnected\n")
            append("  realRobotEnabled = $realRobotEnabled\n")
            append("  powerOnRobot = $powerOnRobot\n")
            append("  emergencyStopped = $emergencyStopped\n")
            append("  securityStopped = $securityStopped\n")
            append("  programRunning = $programRunning\n")
            append("  programPaused = $programPaused\n")
            append("  robotMode = ${robotMode ?: "N/A"}\n")
            append("  controlMode = ${controlMode ?: "N/A"}\n")
            append("  speedFraction = $speedFraction\n")
            append("  speedScaling = $speedScaling\n")
            append("  speedFractionLimit = $speedFractionLimit\n")
            append("  reserved = $reserved")
        }
    }
}