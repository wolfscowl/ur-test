package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.AdditionalInfo
import com.wolfscowl.ur_client.model.robot_state.RobotState
import java.nio.ByteBuffer

internal object AdditionalInfoDecoder: StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState = AdditionalInfo(
        tpButtonState = bb.get().toInt() and 0xFF,
        freedriveButtonEnabled = bb.get().toInt() == 1,
        ioEnabledFreeDrive =  bb.get().toInt() == 1,
        reserved = bb.get().toInt() and 0xFF
    )
}