package com.wolfscowl.ur_client.core.internal.decoder

import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.AdditionalInfoDecoder
import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.CartesianInfoDecoder
import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.ForceModeDataDecoder
import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.JointDataDecoder
import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.KinematicsInfoDecoder
import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.MasterBoardDataDecoder
import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.RobotModeDataDecoder
import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.StateDecoder
import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.ToolCommunicationInfoDecoder
import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.ToolDataDecoder
import com.wolfscowl.ur_client.core.internal.decoder.state_decoder.ToolModeInfoDecoder



// sub package of main message: ROBOT_STATE
internal enum class RobotStatePackageType(val code: Int, val decoder: StateDecoder?) {
    ROBOT_MODE_DATA(0, RobotModeDataDecoder),
    JOINT_DATA(1, JointDataDecoder),
    TOOL_DATA(2, ToolDataDecoder),
    MASTERBOARD_DATA(3, MasterBoardDataDecoder),
    CARTESIAN_INFO(4, CartesianInfoDecoder),
    KINEMATICS_INFO(5, KinematicsInfoDecoder),
    CONFIGURATION_DATA(6,
        com.wolfscowl.ur_client.core.internal.decoder.state_decoder.ConfigurationDataDecoder
    ),
    FORCE_MODE_DATA(7, ForceModeDataDecoder),
    ADDITIONAL_INFO(8, AdditionalInfoDecoder),
    NEEDED_FOR_CALIB_DATA(9, null),     // It is used internally by Universal Robots software only
    SAFETY_DATA(10, null),              // It is used internally by Universal Robots software only
    TOOL_COMM_INFO(11, ToolCommunicationInfoDecoder),
    TOOL_MODE_INFO(12, ToolModeInfoDecoder),
    UNDEFINED(-9999, null);             // Code 14

    companion object {
        fun fromCode(code: Int): RobotStatePackageType = entries.find { it.code == code } ?: UNDEFINED
    }
}


