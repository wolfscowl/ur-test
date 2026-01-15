package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.CartesianInfo
import com.wolfscowl.ur_client.model.robot_state.RobotState
import java.nio.ByteBuffer

internal object CartesianInfoDecoder: StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState = CartesianInfo(
        x = bb.double,
        y = bb.double,
        z = bb.double,
        rX = bb.double,
        rY = bb.double,
        rZ = bb.double,
        tcpOffsetX = bb.double,
        tcpOffsetY = bb.double,
        tcpOffsetZ = bb.double,
        tcpOffsetRx = bb.double,
        tcpOffsetRy = bb.double,
        tcpOffsetRz = bb.double
    )
}