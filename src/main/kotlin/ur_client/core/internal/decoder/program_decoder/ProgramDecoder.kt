package com.wolfscowl.ur_client.core.internal.decoder.program_decoder

import com.wolfscowl.ur_client.model.robot_program_state.RobotProgramState
import java.nio.ByteBuffer

internal interface ProgramDecoder {
    fun decode(bb: ByteBuffer): RobotProgramState?
}