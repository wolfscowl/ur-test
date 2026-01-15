package com.wolfscowl.ur_client.model.robot_state


import com.wolfscowl.ur_client.core.internal.util.Util.formatString

// ROBOT_STATE_PACKAGE_TYPE_CONFIGURATION_DATA = 6
data class ConfigurationData (
    // ToDo make extra values (base,shoulder,elbow, wrist1, wrist2, wrist3) for ConfigurationData
    val jointConfigurationData: List<JointConfigurationData>,
    val vJointDefault: Double,      // double
    val aJointDefault: Double,      // double
    val vToolDefault: Double,       // double
    val aToolDefault: Double,       // double
    val eqRadius: Double,           // double
    val masterboardVersion: Int,    // int32_t
    val controllerBoxType: Int,     // int32_t
    val robotType: Int,             // int32_t
    val robotSubType: Int,          // int32_t
): RobotState {
    override fun toString(): String {
        return buildString {
            append("-=ConfigurationData=-\n")
            append("  vJointDefault = $vJointDefault\n")
            append("  aJointDefault = $aJointDefault\n")
            append("  vToolDefault = $vToolDefault\n")
            append("  aToolDefault = $aToolDefault\n")
            append("  eqRadius = $eqRadius\n")
            append("  jointConfigurationData = ")
            jointConfigurationData.forEach { jointConfig ->
                append("\n")
                append(jointConfig.toString().formatString(false, 4))
            }
        }
    }

    data class JointConfigurationData(
        val jointMinLimit: Double,          // double
        val jointMaxLimitt: Double,         // double
        val jointMaxSpeed: Double,          // double
        val jointMaxAcceleration: Double,   // double
        val DHa: Double,                    // double
        val Dhd: Double,                    // double
        val DHalpha: Double,                // double
        val DHtheta: Double                 // double

    ) {
        override fun toString(): String {
            return buildString {
                append("-=JointConfigurationData=-\n")
                append("  jointMinLimit = $jointMinLimit\n")
                append("  jointMaxLimitt = $jointMaxLimitt\n")
                append("  jointMaxSpeed = $jointMaxSpeed\n")
                append("  jointMaxAcceleration = $jointMaxAcceleration\n")
                append("  DHa = $DHa\n")
                append("  Dhd = $Dhd\n")
                append("  DHalpha = $DHalpha\n")
                append("  DHtheta = $DHtheta")
            }
        }
    }

}

