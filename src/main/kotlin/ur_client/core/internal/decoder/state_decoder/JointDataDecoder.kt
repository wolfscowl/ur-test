package com.wolfscowl.ur_client.core.internal.decoder.state_decoder

import com.wolfscowl.ur_client.model.robot_state.JointData
import com.wolfscowl.ur_client.model.robot_state.RobotState
import com.wolfscowl.ur_client.model.robot_state.mode.JointMode
import java.nio.ByteBuffer

internal object JointDataDecoder:
    StateDecoder {
    override fun decode(bb: ByteBuffer): RobotState {
        val joints = (1..6).map {
            JointData.Joint(
                qActual = bb.double,
                qTarget = bb.double,
                qdActual = bb.double,
                iActual = bb.float.toDouble(),
                vActual = bb.float.toDouble(),
                tMotor = bb.float.toDouble(),
                tMicro = bb.float.toDouble(),
                jointMode = JointMode.fromCode(bb.get().toInt() and 0xFF)
            )
        }
        return JointData(
            base = joints[0],
            shoulder = joints[1],
            elbow = joints[2],
            wrist1 = joints[3],
            wrist2 = joints[4],
            wrist3 = joints[5]
        )
    }
}