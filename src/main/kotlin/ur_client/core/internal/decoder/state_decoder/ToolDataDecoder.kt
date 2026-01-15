package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.RobotState
import com.wolfscowl.ur_client.model.robot_state.ToolData
import com.wolfscowl.ur_client.model.robot_state.mode.ToolMode
import java.nio.ByteBuffer

internal object ToolDataDecoder: StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState = ToolData(
        analogInputRange2 = bb.get().toInt() and 0xFF,
        analogInputRange3 = bb.get().toInt() and 0xFF,
        analogInput2 = bb.double,
        analogInput3 = bb.double,
        toolVoltage48v = bb.float.toDouble(),
        toolOutputVoltage = bb.get().toInt() and 0xFF,
        toolCurrent = bb.float.toDouble(),
        toolTemperature = bb.float.toDouble(),
        toolMode = ToolMode.fromCode( bb.get().toInt() and 0xFF )
    )
}