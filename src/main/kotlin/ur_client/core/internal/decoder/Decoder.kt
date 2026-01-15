package com.wolfscowl.ur_client.core.internal.decoder

import com.wolfscowl.ur_client.core.internal.Watchdog
import com.wolfscowl.ur_client.core.internal.util.Util
import com.wolfscowl.ur_client.core.internal.util.Util.log
import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_program_state.RobotProgramState
import com.wolfscowl.ur_client.model.robot_state.RobotState
import java.io.EOFException
import java.io.InputStream
import java.net.SocketTimeoutException
import java.nio.ByteBuffer
import kotlin.system.measureTimeMillis

internal class Decoder(
    val input: InputStream,
) {
    val headerSize = 5
    val buffer = ByteArray(2048)

    /*
    ============= HEADER =============
    4 byte = Length of overall package
    1 byte = Package (Message) Type
    ==================================
    */

    fun decodeMessage(): MessageType {
        // read header
        //println("\n     -> READ HEADER <-")
        readStream(input, buffer, headerSize)
        val bb = ByteBuffer.wrap(buffer, 0, headerSize)
        val messageSize = bb.int
        val messageCode = bb.get().toInt() and 0xFF
        // read payload
        val payloadSize = messageSize - headerSize
        //log("messageCode: $messageCode | payload size: $payloadSize")
        //log("      -> READ PAYLOAD <-")
        readStream(input, buffer, payloadSize)
        // create Message
        val messageType = MessageType(messageCode,buffer.copyOfRange(0,payloadSize))
        return messageType
    }



    fun decodeRobotMessage(message: MessageType.RobotMessage): RobotMessage? {
        // decode robot message payload
        val bb = ByteBuffer.wrap(message.payload, 0, message.payload.size)
        val robotMessageCode = bb.get(9).toInt()
        //log("robotMessageCode: $robotMessageCode")
        message.consumed = message.payload.size
        return RobotMessageType.fromCode(robotMessageCode).decoder?.decode(bb)
    }


    fun decodeRobotStateMessage(message: MessageType.RobotState): RobotState? {
        if (message.consumed == message.payload.size)
            return null
        // decode robot state package header
        var bb = ByteBuffer.wrap(message.payload, message.consumed, headerSize)
        val packageSize = bb.int
        val packagePayloadSize = packageSize - headerSize
        val packageCode = bb.get().toInt() and 0xFF
        //log("packageCode: $packageCode | packageSize: $packageSize")
        val packageType = RobotStatePackageType.fromCode(packageCode)
        // log("Consumed: " + message.consumed + " PayLoadSize: " +  message.payload.size  + "   packageSize:  " + packageSize)
        // decode robot state package payload
        val packagePayload =
            message.payload.copyOfRange(
                message.consumed + headerSize,
                message.consumed + headerSize + packagePayloadSize
            )
        bb = ByteBuffer.wrap(packagePayload, 0, packagePayloadSize)
        message.consumed += packageSize
        return packageType.decoder?.decode(bb)
    }


    fun decodeRobotProgramStateMessage(message: MessageType.RobotProgramState): RobotProgramState? {
        // decode robot program state payload
        val bb = ByteBuffer.wrap(message.payload, 0, message.payload.size)
        val robotMessageCode = bb.get(9).toInt()
        message.consumed = message.payload.size
        return ProgramStateMessageType.fromCode(robotMessageCode).decoder?.decode(bb)
    }

    private fun readStream(input: InputStream, buffer: ByteArray, length: Int) {
        var bytesRead = 0
        while (bytesRead < length) {
            try {
                val result = input.read(buffer, bytesRead, length - bytesRead)
                if (result == -1) throw EOFException("Socket closed by remote")
                bytesRead += result
            } catch (e: SocketTimeoutException) {
            }
        }
    }


//    private fun readStream(input: InputStream, buffer: ByteArray, length: Int) {
//        var bytesRead = 0
//        while (bytesRead < length) {
//            val result = input.read(buffer, bytesRead, length - bytesRead)
//            if (result == -1)
//                throw EOFException("Decoder: No Bytes in input stream available to read. \nBytes that should be read: $length \nBytes that have been read: $bytesRead")
//            bytesRead += result
//        }
//    }

}