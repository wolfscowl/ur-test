package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.MasterBoardData
import com.wolfscowl.ur_client.model.robot_state.RobotState
import com.wolfscowl.ur_client.model.robot_state.mode.SafetyMode
import java.nio.ByteBuffer

internal object MasterBoardDataDecoder: StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState {
        val containsEuromap67Data =  bb.get(62).toInt() == 1
        return MasterBoardData(
            digitalInputBits = bb.int,
            digitalOutputBits = bb.int,
            analogInputRange0 = bb.get().toInt() and 0xFF,
            analogInputRange1 = bb.get().toInt() and 0xFF,
            analogInput0 = bb.double,
            analogInput1 = bb.double,
            analogOutputDomain0 = bb.get().toInt(),
            analogOutputDomain1 = bb.get().toInt(),
            analogOutput0 = bb.double,
            analogOutput1 = bb.double,
            masterBoardTemperature = bb.float.toDouble(),
            robotVoltage48V = bb.float.toDouble(),
            robotCurrent = bb.float.toDouble(),
            masterIOCurrent = bb.float.toDouble(),
            safetyMode = SafetyMode.fromCode( bb.get().toInt() and 0xFF ),
            inReducedMode = bb.get().toInt() and 0xFF,
            euromap67InterfaceInstalled = bb.get().toInt() == 1,
            euromapInputBits = if (containsEuromap67Data) (bb.int.toLong() and 0xFFFFFFFFL)  else null,
            euromapOutputBits = if (containsEuromap67Data) (bb.int.toLong() and 0xFFFFFFFFL)  else null,
            euromapVoltage24V = if (containsEuromap67Data) (bb.float.toDouble())  else null,
            euromapCurrent = if (containsEuromap67Data) (bb.float.toDouble())  else null,
            operationalModeSelectorInput = bb.apply { position(position() + 4) }.get().toInt() and 0xFF,
            threePositionEnablingDeviceInput = bb.get().toInt() and 0xFF,
        )
    }
}