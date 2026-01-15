package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.ForceModeData
import com.wolfscowl.ur_client.model.robot_state.RobotState
import java.nio.ByteBuffer

internal object ForceModeDataDecoder: StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState = ForceModeData(
        fX = bb.double,
        fY = bb.double,
        fZ = bb.double,
        fRX = bb.double,
        fRY = bb.double,
        fRZ = bb.double,
        robotDexterity = bb.double
    )
}