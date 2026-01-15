package com.wolfscowl.ur_client.model.robot_state

import com.wolfscowl.ur_client.model.robot_state.mode.SafetyMode

// ROBOT_STATE_PACKAGE_TYPE_MASTERBOARD_DATA = 3
data class MasterBoardData(
    val digitalInputBits: Int,                  // int
    val digitalOutputBits: Int,                 // int
    val analogInputRange0: Int,                 // unsigned char
    val analogInputRange1: Int,                 // unsigned char
    val analogInput0: Double,                   // double
    val analogInput1: Double,                   // double
    val analogOutputDomain0: Int,               // char
    val analogOutputDomain1: Int,               // char
    val analogOutput0: Double,                  // double
    val analogOutput1: Double,                  // double
    val masterBoardTemperature: Double,         // float
    val robotVoltage48V: Double,                // float
    val robotCurrent: Double,                   // float
    val masterIOCurrent: Double,                // float
    val safetyMode: SafetyMode,                 // unsigned char
    val inReducedMode: Int,                     // uint8_t  ToDO - Check Type  maybe bool
    val euromap67InterfaceInstalled: Boolean,   // char
    val euromapInputBits: Long?,                // uint32_t
    val euromapOutputBits: Long?,               // uint32_t
    val euromapVoltage24V: Double?,             // float
    val euromapCurrent: Double?,                // float
    val operationalModeSelectorInput: Int,      // uint8_t
    val threePositionEnablingDeviceInput: Int,  // uint8_t

): RobotState {
    override fun toString(): String {
        return buildString {
            append("-=MasterBoardData=-\n")
            append("  digitalInputBits = $digitalInputBits\n")
            append("  digitalOutputBits = $digitalOutputBits\n")
            append("  analogInputRange0 = $analogInputRange0\n")
            append("  analogInputRange1 = $analogInputRange1\n")
            append("  analogInput0 = $analogInput0\n")
            append("  analogInput1 = $analogInput1\n")
            append("  analogOutputDomain0 = $analogOutputDomain0\n")
            append("  analogOutputDomain1 = $analogOutputDomain1\n")
            append("  analogOutput0 = $analogOutput0\n")
            append("  analogOutput1 = $analogOutput1\n")
            append("  masterBoardTemperature = $masterBoardTemperature\n")
            append("  robotVoltage48V = $robotVoltage48V\n")
            append("  robotCurrent = $robotCurrent\n")
            append("  masterIOCurrent = $masterIOCurrent\n")
            append("  safetyMode = ${safetyMode ?: "N/A"}\n")
            append("  inReducedMode = $inReducedMode\n")
            append("  euromap67InterfaceInstalled = $euromap67InterfaceInstalled\n")
            append("  euromapInputBits = ${euromapInputBits ?: "N/A"}\n")
            append("  euromapOutputBits = ${euromapOutputBits ?: "N/A"}\n")
            append("  euromapVoltage24V = ${euromapVoltage24V ?: "N/A"}\n")
            append("  euromapCurrent = ${euromapCurrent ?: "N/A"}\n")
            append("  operationalModeSelectorInput = $operationalModeSelectorInput\n")
            append("  threePositionEnablingDeviceInput = $threePositionEnablingDeviceInput")
        }
    }
}