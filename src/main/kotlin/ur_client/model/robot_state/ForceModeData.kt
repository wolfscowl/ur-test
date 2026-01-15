package com.wolfscowl.ur_client.model.robot_state

// ROBOT_STATE_PACKAGE_TYPE_FORCE_MODE_DATA=7
data class ForceModeData (
    val fX: Double,             // double
    val fY: Double,             // double
    val fZ: Double,             // double
    val fRX: Double,            // double
    val fRY: Double,            // double
    val fRZ: Double,            // double
    val robotDexterity: Double  // double
): RobotState {
    override fun toString(): String {
        return buildString {
            append("-=ForceModeData=-\n")
            append("  fX = $fX\n")
            append("  fY = $fY\n")
            append("  fZ = $fZ\n")
            append("  fRX = $fRX\n")
            append("  fRY = $fRY\n")
            append("  fRZ = $fRZ\n")
            append("  robotDexterity = $robotDexterity")
        }
    }
}