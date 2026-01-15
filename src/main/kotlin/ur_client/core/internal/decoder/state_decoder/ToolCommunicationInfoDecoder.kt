package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.RobotState
import com.wolfscowl.ur_client.model.robot_state.ToolCommunicationInfo
import java.nio.ByteBuffer

internal object ToolCommunicationInfoDecoder: StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState = ToolCommunicationInfo(
        toolCommunicationIsEnabled = bb.get().toInt() == 1,
        baudRate = bb.int,
        parity = bb.int,
        stopBits = bb.int,
        rxIdleChars = bb.float.toDouble(),
        txIdleChars = bb.float.toDouble()
    )
}