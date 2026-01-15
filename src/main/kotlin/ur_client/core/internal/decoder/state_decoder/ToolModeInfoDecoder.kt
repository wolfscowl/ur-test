package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.RobotState
import com.wolfscowl.ur_client.model.robot_state.ToolModeInfo
import java.nio.ByteBuffer

internal object ToolModeInfoDecoder: StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState = ToolModeInfo(
        outputMode = bb.get().toInt() and 0xFF,
        digitalOutputModeOutput0 = bb.get().toInt() and 0xFF,
        digitalOutputModeOutput1 = bb.get().toInt() and 0xFF
    )
}