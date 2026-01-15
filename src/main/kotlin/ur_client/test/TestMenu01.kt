package com.wolfscowl.ur_client.test

import com.wolfscowl.ur_client.UR
import com.wolfscowl.ur_client.model.element.JointPosition

//// 139.6.77.184
//fun main() {
//    // 139.6.77.184
//    val ur = UR("192.168.178.32",30001)
//    ur.connect()
//    Thread.sleep(1000)
//    if (ur.isConnected) {
//        println("\nConnected to robot!")
//        while (true) {
//            println("\nChoose a command:")
//            println("1. Move robot arm 5 degrees to the left")
//            println("2. Move robot arm 5 degrees to the right")
//            println("3. 2FG7 - Close gripper ")
//            println("4. 2FG7 - Open gripper")
//            println("5. 2FG7 - Test script")
//            println("7. RG6 - Close gripper")
//            println("8. RG6 - Open gripper")
//            println("0. Exit")
//            val input = readLine()
//
//            when (input) {
//                "1" -> moveLeft1(ur)
//                "2" -> moveRight1(ur)
//                "3" -> closeGripper2FG71(ur)
//                "4" -> openGripper2FG71(ur)
//                "5" -> testScript1(ur)
//                "7" -> closeGripperRG61(ur)
//                "8" -> openGripperRG61(ur)
//                "0" -> break
//                else -> println("Invalid input")
//            }
//        }
//    } else {
//        println("Robot connection failed")
//    }
//}
//
//fun moveLeft1(ur: UR) {
//    println("\nCMD: arm to the left")
//    val joints = ur.jointPosition
//    //println("JointPosition:\n" + joints)
//    //println("Press Key to send new JointData: ")
//    joints?.let {
//        ur.arm.movej(
//            q = JointPosition(
//                base = it.base - Math.toRadians(5.0),
//                shoulder = it.shoulder,
//                elbow = it.elbow,
//                wrist1 = it.wrist1,
//                wrist2 = it.wrist2,
//                wrist3 = it.wrist3
//            ),
//            a = 1.2,
//            v = 0.9,
//            t = 0.0,
//            r = 0.0
//        )
//    }
//}
//
//fun moveRight1(ur: UR) {
//    println("\nCMD: arm to the right")
//    val joints = ur.jointPosition
//    //println("JointPosition:\n" + joints)
//    //println("Press Key to send new JointData: ")
//    joints?.let {
//        ur.arm.movej(
//            q = JointPosition(
//                base = it.base + Math.toRadians(5.0),
//                shoulder = it.shoulder,
//                elbow = it.elbow,
//                wrist1 = it.wrist1,
//                wrist2 = it.wrist2,
//                wrist3 = it.wrist3
//            ),
//            a = 1.2,
//            v = 0.9,
//            t = 0.0,
//            r = 0.0
//        )
//    }
//}
//
//fun closeGripper2FG71(ur: UR) {
//    ur.sendURScript("""
//        def closeGripper():
//            global ip_address = "192.168.12.146"
//
//            # min 0 max 37
//            global width_closed = 0.0
//
//            # min 20 N max 85 N
//            global force = 50
//
//            # min 10% max 100%
//            global speed = 10
//
//            global tool_index = 0
//
//            global gripper = rpc_factory("xmlrpc", "http://192.168.12.146:41414")
//
//            def grip_ext():
//                gripper.twofg_grip_external(tool_index,width_closed,force,speed)
//            end
//
//            grip_ext()
//        end
//    """.trimIndent())
//}
//
//fun openGripper2FG71(ur: UR) {
//    ur.sendURScript("""
//        def openGripper():
//            global ip_address = "192.168.12.146"
//
//            # min 0 max 37
//            global width_open = 30.0
//
//            # min 20 N max 85 N
//            global force = 50
//
//            # min 10% max 100%
//            global speed = 10
//
//            global tool_index = 0
//
//            global gripper = rpc_factory("xmlrpc", "http://192.168.12.146:41414")
//
//            def grip_ext():
//                gripper.twofg_grip_external(tool_index,width_open,force,speed)
//            end
//
//            grip_ext()
//        end
//    """.trimIndent())
//}
//
//fun closeGripperRG61(ur: UR) {
//    ur.sendURScript("""
//        def closeGripper():
//            global ip_address = "192.168.12.146"
//
//            # min 0 max 37
//            global width_closed = 0.0
//
//            # min 20 N max 85 N
//            global force = 50.0
//
//            # min 10% max 100%
//            global speed = 10
//
//            global tool_index = 0
//
//            global gripper = rpc_factory("xmlrpc", "http://192.168.12.146:41414")
//
//            def grip_ext():
//                gripper.rg_grip(tool_index,width_closed,force)
//            end
//
//            grip_ext()
//        end
//    """.trimIndent())
//}
//
//fun openGripperRG61(ur: UR) {
//    ur.sendURScript("""
//        def openGripper():
//            global ip_address = "192.168.12.146"
//
//            # min 0 max 37
//            global width_open = 30.0
//
//            # min 20 N max 85 N
//            global force = 50.0
//
//            # min 10% max 100%
//            global speed = 10
//
//            global tool_index = 0
//
//            global gripper = rpc_factory("xmlrpc", "http://192.168.12.146:41414")
//
//            def grip_ext():
//                gripper.rg_grip(tool_index,width_open,force)
//            end
//
//            grip_ext()
//        end
//    """.trimIndent())
//}
//
//
//
//fun testScript1(ur: UR) {
//    ur.sendURScript(Script01.script)
//}