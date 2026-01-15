package com.wolfscowl.ur_client.model.robot_message


import com.wolfscowl.ur_client.model.robot_message.type.MessageSource


interface RobotMessage {
    val timestamp: ULong                        // uint64_t
    val source: MessageSource                   // char
}