package com.wolfscowl.ur_client.model.robot_message.type

enum class MessageSource(val code: Int, val description: String) {
    // Message Sources
    MESSAGE_SOURCE_JOINT_0_FPGA(100, "Joint 0 source via FPGA"),
    MESSAGE_SOURCE_JOINT_0_A(110, "Joint 0 source A"),
    MESSAGE_SOURCE_JOINT_0_B(120, "Joint 0 source B"),
    MESSAGE_SOURCE_JOINT_1_FPGA(101, "Joint 1 source via FPGA"),
    MESSAGE_SOURCE_JOINT_1_A(111, "Joint 1 source A"),
    MESSAGE_SOURCE_JOINT_1_B(121, "Joint 1 source B"),
    MESSAGE_SOURCE_JOINT_2_FPGA(102, "Joint 2 source via FPGA"),
    MESSAGE_SOURCE_JOINT_2_A(112, "Joint 2 source A"),
    MESSAGE_SOURCE_JOINT_2_B(122, "Joint 2 source B"),
    MESSAGE_SOURCE_JOINT_3_FPGA(103, "Joint 3 source via FPGA"),
    MESSAGE_SOURCE_JOINT_3_A(113, "Joint 3 source A"),
    MESSAGE_SOURCE_JOINT_3_B(123, "Joint 3 source B"),
    MESSAGE_SOURCE_JOINT_4_FPGA(104, "Joint 4 source via FPGA"),
    MESSAGE_SOURCE_JOINT_4_A(114, "Joint 4 source A"),
    MESSAGE_SOURCE_JOINT_4_B(124, "Joint 4 source B"),
    MESSAGE_SOURCE_JOINT_5_FPGA(105, "Joint 5 source via FPGA"),
    MESSAGE_SOURCE_JOINT_5_A(115, "Joint 5 source A"),
    MESSAGE_SOURCE_JOINT_5_B(125, "Joint 5 source B"),
    MESSAGE_SOURCE_TOOL_FPGA(106, "Tool source via FPGA"),
    MESSAGE_SOURCE_TOOL_A(116, "Tool source A"),
    MESSAGE_SOURCE_TOOL_B(126, "Tool source B"),
    MESSAGE_SOURCE_EUROMAP_FPGA(107, "Euromap source via FPGA"),
    MESSAGE_SOURCE_EUROMAP_A(117, "Euromap source A"),
    MESSAGE_SOURCE_EUROMAP_B(127, "Euromap source B"),
    MESSAGE_SOURCE_TEACH_PENDANT_A(108, "Teach pendant source A"),
    MESSAGE_SOURCE_TEACH_PENDANT_B(118, "Teach pendant source B"),
    MESSAGE_SOURCE_SCB_FPGA(40, "SCB source via FPGA"),
    MESSAGE_SAFETY_PROCESSOR_UA(20, "Safety processor unit A"),
    MESSAGE_SAFETY_PROCESSOR_UB(30, "Safety processor unit B"),
    MESSAGE_SOURCE_ROBOTINTERFACE(-2, "Robot interface source"),
    MESSAGE_SOURCE_RTMACHINE(-3, "RT machine source"),
    MESSAGE_SOURCE_SIMULATED_ROBOT(-4, "Simulated robot source"),
    MESSAGE_SOURCE_GUI(-5, "Graphical user interface source"),
    MESSAGE_SOURCE_CONTROLLER(7, "Controller source"),
    MESSAGE_SOURCE_RTDE(8, "RTDE source"),

    // Message Types
    MESSAGE_TYPE_DISCONNECT(-1, "Disconnect message"),
    MESSAGE_TYPE_ROBOT_STATE(16, "Robot state message"),
    MESSAGE_TYPE_ROBOT_MESSAGE(20, "General robot message"),
    MESSAGE_TYPE_HMC_MESSAGE(22, "HMC message"),
    MESSAGE_TYPE_MODBUS_INFO_MESSAGE(5, "Modbus info message"),
    MESSAGE_TYPE_SAFETY_SETUP_BROADCAST_MESSAGE(23, "Safety setup broadcast message"),
    MESSAGE_TYPE_SAFETY_COMPLIANCE_TOLERANCES_MESSAGE(24, "Safety compliance tolerances message"),
    MESSAGE_TYPE_PROGRAM_STATE_MESSAGE(25, "Program state message"),

    MESSAGE_TYPE_SOURCE_UNDEFINED(-9999, "Undefined message type or source");

    companion object {
        fun fromCode(code: Int): MessageSource = MessageSource.entries.find { it.code == code } ?: MESSAGE_TYPE_SOURCE_UNDEFINED
    }
}