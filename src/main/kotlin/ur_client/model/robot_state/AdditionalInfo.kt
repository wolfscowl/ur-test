package com.wolfscowl.ur_client.model.robot_state


// ROBOT_STATE_PACKAGE_TYPE_ADDITIONAL_INFO = 8
data class AdditionalInfo(
    val tpButtonState: Int,                 // unsigned char  ToDo - ?teachButtonPressed? ?? maybe bool ??
    val freedriveButtonEnabled: Boolean,    // bool           ToDo - ?teachButtonEnabled? ??
    val ioEnabledFreeDrive: Boolean,        // bool
    val reserved: Int                       // unsigned char
): RobotState {
    override fun toString(): String {
        return buildString {
            append("-=AdditionalInfo=-\n")
            append("  tpButtonState = $tpButtonState\n")
            append("  freedriveButtonEnabled = $freedriveButtonEnabled\n")
            append("  ioEnabledFreeDrive = $ioEnabledFreeDrive\n")
            append("  reserved = $reserved")
        }
    }
}
