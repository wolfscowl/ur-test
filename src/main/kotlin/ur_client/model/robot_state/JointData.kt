package com.wolfscowl.ur_client.model.robot_state

import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.model.robot_state.mode.JointMode

// ROBOT_STATE_PACKAGE_TYPE_JOINT_DATA = 1
data class JointData(
    val base: Joint,
    val shoulder: Joint,
    val elbow: Joint,
    val wrist1: Joint,
    val wrist2: Joint,
    val wrist3: Joint,
): RobotState {
    override fun toString(): String {
        return buildString {
            append("-=JointData=-\n")
            append("  -=Base=-\n")
            append(base.toString().formatString(true,2) + "\n")
            append("  -=Shoulder=-\n")
            append(shoulder.toString().formatString(true,2) + "\n")
            append("  -=Elbow=-\n")
            append(elbow.toString().formatString(true,2) + "\n")
            append("  -=Wrist1=-\n")
            append(wrist1.toString().formatString(true,2) + "\n")
            append("  -=Wrist2=-\n")
            append(wrist2.toString().formatString(true,2) + "\n")
            append("  -=Wrist3=-\n")
            append(wrist3.toString().formatString(true,2))
        }
    }

    data class Joint(
        val qActual: Double,        // double   // current position (radiant)
        val qTarget: Double,        // double   // current target  (radiant)
        val qdActual: Double,       // double   // current velocity (rad/sec)
        val iActual: Double,        // float    // current amperage of the motor
        val vActual: Double,        // float    // current voltage of the motor
        val tMotor: Double,         // float    // current temperature of the motor
        val tMicro: Double,         // float    // current temperature of the microcontroller
        val jointMode: JointMode,   // uint8_t  // joint mode
    ) {
        override fun toString(): String {
            return buildString {
                append("-=Joint=-\n")
                append("  qActual = $qActual\n")
                append("  qTarget = $qTarget\n")
                append("  qdActual = $qdActual\n")
                append("  iActual = $iActual\n")
                append("  vActual = $vActual\n")
                append("  tMotor = $tMotor\n")
                append("  tMicro = $tMicro\n")
                append("  jointMode = $jointMode")
            }
        }
    }
}