package com.wolfscowl.ur_client.core.internal.decoder

internal sealed class MessageType(val code: Int, val payload: ByteArray, var consumed: Int = 0) {
    class RobotState(payload: ByteArray) : MessageType(16, payload)
    class RobotMessage(payload: ByteArray) : MessageType(20, payload)
    class RobotProgramState(payload: ByteArray) : MessageType(25, payload)
    class Undefined(payload: ByteArray) : MessageType(-9999, payload)

    companion object {
        operator fun invoke(code: Int, payload: ByteArray): MessageType {
            return when (code) {
                16 -> RobotState(payload)
                20 -> RobotMessage(payload)
                25 -> RobotProgramState(payload)
                else -> Undefined(payload)
            }

        }
    }
}



