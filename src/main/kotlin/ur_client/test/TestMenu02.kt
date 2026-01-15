package com.wolfscowl.ur_client.test

import com.wolfscowl.ur_client.UR
import com.wolfscowl.ur_client.interfaces.state.ArmState
import com.wolfscowl.ur_client.interfaces.state.await
import com.wolfscowl.ur_client.core.internal.config.Default.CMD_TIMEOUT
import com.wolfscowl.ur_client.core.internal.util.Util.log
import com.wolfscowl.ur_client.interfaces.state.awaitBlocking
import com.wolfscowl.ur_client.interfaces.tool.OnRobotTFG
import com.wolfscowl.ur_client.interfaces.tool.OnRobotRG
import com.wolfscowl.ur_client.interfaces.tool.OnRobotVG
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.WatchdogConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    //val urHost = "192.168.178.32"
    //val urHost = "192.168.12.145"
    val urHost = "192.168.12.243"
    //val urHost = "192.168.1.15"
    //val urHost = "192.168.56.101"
    val onRobotHost = "192.168.12.245"

    val ur = UR(host = urHost, logRobotMessages = false, logRobotStates = false)
    ur.connect()

    val onRobotRG6 = ur.attachToolOnRobotRG(host = onRobotHost, toolIndex = 0)
    val onRobot2FG7 = ur.attachToolOnRobotTFG(host = onRobotHost, toolIndex = 0)
    val onRobotVG = ur.attachToolOnRobotVG(host = onRobotHost, toolIndex = 0)

    Thread.sleep(1000)
    if (ur.isConnected) {
        println("\nConnected to robot!")
        while (true) {
            println("\nChoose a command:")
            println("a. Power ON")
            println("b. Power OFF")
            println("1. Move robot arm 5 degrees to the left")
            println("2. Move robot arm 5 degrees to the right")
            println("3. 2FG7 - Close gripper ")
            println("4. 2FG7 - Open gripper")
            println("5. RG6 - Close gripper")
            println("6. RG6 - Open gripper")
            println("7. VG - Close gripper")
            println("8. VG - Open gripper")
            println("9. VG - Close gripper with seek")
            println("10. Move robot arm multiple times 1")
            println("11. Move robot arm multiple times 2")
            println("12. Program Test 01")
            println("13. RuntimeException")
            println("14. SafetyModeStop")
            println("15. MoveWithPayload")
            println("16. CustomScript")
            println("17. Set TCP Offset")
            println("18. MoveC")
            println("19. Test moveP")
            println("20. MoveJ with TCP Test")
            println("21. Freedive Mode")
            println("22. Protection_Stop")
            println("23. fetchSafetyStatus")
            println("24. Unlock Protection Stop")
            println("25. moveL With Joint Position")
            println("0. Exit")
            val input = readLine()

            when (input) {
                "a" -> powerOn(ur)
                "b" -> powerOff(ur)
                "1" -> moveLeft(ur)
                "2" -> moveRight(ur)
                "3" -> closeGripper2FG7(onRobot2FG7)
                "4" -> openGripper2FG7(onRobot2FG7)
                "5" -> closeGripperRG6(onRobotRG6)
                "6" -> openGripperRG6(onRobotRG6)
                "7" -> closeGripperVG(onRobotVG)
                "8" -> openGripperVG(onRobotVG)
                "9" -> closeGripperVGSeek(onRobotVG)
                "10" -> moveMultipleTimes1(ur)
                "11" -> moveMultipleTimes2(ur)
                "12" -> programTest01(ur,onRobotVG)
                "13" -> runtimeException(ur)
                "14" -> safetyModeStop(ur)
                "15" -> moveWithPayload(ur)
                "16" -> customScript(ur)
                "17" -> setTcpOffset(ur)
                "18" -> moveC(ur)
                "19" -> moveP(ur)
                "20" -> moveJWithTCP(ur)
                "21" -> freedriveMode(ur)
                "22" -> protectionStop(ur)
                "23" -> fetchSafetyStatus(ur)
                "24" -> unlockProtectionStop(ur)
                "25" -> moveLWithJointPosition(ur)
                "0" -> break
                else -> println("Invalid input")
            }
        }
    } else {
        println("Robot connection failed")
    }
}

fun powerOn(ur: UR) {
    ur.powerOn {  test -> println(test) }
}

fun powerOff(ur: UR) {
    ur.powerOff()
}

fun unlockProtectionStop(ur: UR){
    ur.unlockProtectiveStop{
        println(it)
    }
}

fun fetchSafetyStatus(ur: UR) {
    ur.fetchSafetyStatus { println("SafetyStatus $it") }
}

// works
//p = Pose(
//x = 0.2329,
//y = 0.0,
//z = 1.0594,
//rx = -1.2091,
//ry = 1.2091,
//rz = -1.2091
//),


// work not
//p = Pose(
//x = 0.2329,
//y = -0.0,
//z = 1.06939,
//rx = -1.2091995763071834,
//ry = 1.2091995763071832,
//rz = -1.2091995760591727
//),


//p = Pose(
//x = 0.2329,
//y = -2.2532549046026504E-6,
//z = 1.029393111000832,
//rx = -1.2091995763071972,
//ry = 1.209199576307196,
//rz = -1.2091995760591658
//),

//x = 0.2329 m
//y = 0.0 m
//z = 1.0794000000000001 m
//rx = -1.209199576307181 rad
//ry = 1.209199576307185 rad
//rz = -1.2091995760591703 rad

fun freedriveMode(ur: UR) {
    println("\nCMD: RUN FreedriveMode")
    val script =
        """
            freedrive_mode()
            sleep(10.0)
        """.trimIndent()
    ur.runURScript(
        script = script,
        cmdTimeout = 1000L,
        onFinished = {state -> log("##OnFinished##\n$state")}
    )


}


fun protectionStop(ur: UR) {
    GlobalScope.launch {
        val state2 = ur.arm.movej(
            q = JointPosition(
                base = Math.toRadians(390.0),
                shoulder = Math.toRadians(90.0),
                elbow = Math.toRadians(90.0),
                wrist1 = Math.toRadians(90.0),
                wrist2 = Math.toRadians(90.0),
                wrist3 = Math.toRadians(90.0)
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0,
            onFinished = {state -> log("##OnFinished##\n$state")}
        ).await()
        println("AmrState: \n$state2")
    }
}



fun moveJWithTCP(ur: UR) {
    println("\nCMD: RUN TCP Pose TEST")
    val joints = ur.jointPosition
    //println("JointPosition:\n" + joints)
    //println("Press Key to send new JointData: ")
        ur.arm.movej(
            p = Pose(
            x = 0.2329,
            y = 0.0,
            z = 0.8794,
            rx = -1.209,
            ry = 1.2091,
            rz = -1.209
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0,
            //onChange = {state -> log("##OnChange##\n$state")},
            onFinished = {state -> log("##OnFinished##\n$state")}
        )
}


fun setTcpOffset(ur: UR) {
    println("\nCMD: TCP Offset")
    ur.arm.setTcpOffset(
        p = Pose(0.3,0.3,0.0,0.0,0.0,0.0),
        cmdTimeout = 1000L,
        onChange = {state -> log("##OnChange##\n$state")},
        onFinished = {state -> log("##OnFinished##\n$state")}
    )
}

fun customScript(ur: UR) {
    println("\nCMD: customScript")
    val joints = ur.jointPosition
    val script = joints?.let {
        """
            movej([${joints.base - Math.toRadians(5.0)},${joints.shoulder},${joints.elbow},${joints.wrist1},${joints.wrist2},${joints.wrist3}], a=1.2, v=0.9,t=0, r=0)
            set_tcp(p[0.0,0.0,0.2,0.0,0.0,0.0]3)
            send_event("error","BadValue","Fix it")
            send_event("error","Keine Ahnung","Was auch immer")
        """.trimIndent()
    } ?: ""
    ur.runURScript(
        script = script,
        cmdTimeout = 1000L,
        onChange = {state -> log("##OnChange##\n$state")},
        onFinished = {state -> log("##OnFinished##\n$state")})
}

fun moveC(ur: UR) {
    println("\nCMD: MoveC")
    val waypoint1 = Pose(0.099163139545, -0.300842890224, 0.200003216988, -0.000016941848, 3.140878186587, 0.003550956684)
    val poseVia = Pose(-0.011160938995, -0.346202586667, 0.298294494700, 0.147840486216, 3.014629111440, -0.351974988188)
    val poseTo = Pose(-0.168786312094, -0.312803512166, 0.272436736311, 0.872932599069, 2.972675226354, -0.266027262034)

    for (i in 0..4) {
        ur.arm.movel(
            p = waypoint1,
            a = 0.8,
            v = 0.2,
            t = 0.0,
            r = 0.0,
            cmdTimeout = CMD_TIMEOUT,
            onFinished = { state -> log("##OnFinished##\n$state") }
        ).awaitBlocking()

        ur.arm.movec(
            poseVia = poseVia,
            poseTo = poseTo,
            a = 0.8,
            v = 0.2,
            r = 0.0,
            mode = 0,
            cmdTimeout = CMD_TIMEOUT,
            onFinished = { state -> log("##OnFinished##\n$state") }
        ).awaitBlocking()
    }
}

fun moveP(ur: UR) {
    println("\nCMD: customScript")
    val joints = ur.jointPosition
    val waypoint1 = Pose(0.099163139545, -0.300842890224, 0.200003216988, -0.000016941848, 3.140878186587, 0.003550956684)
    val script = joints?.let {
        """
            movep(${waypoint1.toCmdString()}, a=0.5, v=0.5, r=0.5)
        """.trimIndent()
    } ?: ""
    ur.runURScript(
        script = script,
        cmdTimeout = 1000L,
        onChange = {state -> log("##OnChange##\n$state")},
        onFinished = {state -> log("##OnFinished##\n$state")})
}



fun runtimeException(ur: UR) {
    GlobalScope.launch {
        val state = ur.arm.movej(
            q = JointPosition(
                base = Math.toRadians(90.0),
                shoulder = Math.toRadians(90.0),
                elbow = Math.toRadians(90.0),
                wrist1 = Math.toRadians(90.0),
                wrist2 = Math.toRadians(90.0),
                wrist3 = Math.toRadians(90.0)
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0,
            onChange = {state -> log("##OnChange##\n$state")},
            onFinished = {state -> log("##OnFinished##\n$state")}
        ).await()
        println("AmrState: \n$state")
    }
}

fun safetyModeStop(ur: UR) {
    GlobalScope.launch {
        val state = ur.arm.movel(
            p = Pose(
                x = 100.0,
                y = 300.0,
                z = 600.0,
                rx = 2.0,
                ry = 2.0,
                rz = 2.0
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0,
            onFinished = { state -> log("##OnFinished##\n$state")}
        ).await()
        println("ArmState:\n $state")
    }
}


fun moveWithPayload(ur: UR) {
    println("\nCMD: arm to the left")
    println("\n#######################\n############## 1. SET PAYLOAD ####################\n#######################")
    ur.arm.setTargetPayload(1.0f, onFinished = {state -> log("##OnFinished##\n$state")}).awaitBlocking()
    var joints = ur.jointPosition
    println("\n#######################\n############## 2. MOVE JOINTS ####################\n#######################")
    joints?.let {
        ur.arm.movej(
            q = JointPosition(
                base = it.base - Math.toRadians(25.0),
                shoulder = it.shoulder,
                elbow = it.elbow,
                wrist1 = it.wrist1,
                wrist2 = it.wrist2,
                wrist3 = it.wrist3
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0,
            onChange = {state -> log("##OnChange##\n$state")},
            onFinished = {state -> log("##OnFinished##\n$state")}
        ).awaitBlocking()
    }
    println("\n#######################\n############## 3. SET PAYLOAD ####################\n#######################")
    ur.arm.setTargetPayload(2.0f, onFinished = {state -> log("##OnFinished##\n$state")}).awaitBlocking()
    joints = ur.jointPosition
    println("\n#######################\n############## 4. MOVE JOINTS ####################\n#######################")
    joints?.let {
        ur.arm.movej(
            q = JointPosition(
                base = it.base + Math.toRadians(25.0),
                shoulder = it.shoulder,
                elbow = it.elbow,
                wrist1 = it.wrist1,
                wrist2 = it.wrist2,
                wrist3 = it.wrist3
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0,
            onChange = {state -> log("##OnChange##\n$state")},
            onFinished = {state -> log("##OnFinished##\n$state")}
        ).awaitBlocking()
    }
}


fun moveLeft(ur: UR) {
    println("\nCMD: arm to the left")
    val joints = ur.jointPosition
    //println("JointPosition:\n" + joints)
    //println("Press Key to send new JointData: ")
    joints?.let {
        ur.arm.movej(
            q = JointPosition(
                base = it.base - Math.toRadians(5.0),
                shoulder = it.shoulder,
                elbow = it.elbow,
                wrist1 = it.wrist1,
                wrist2 = it.wrist2,
                wrist3 = it.wrist3
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0,
            onChange = {state -> log("##OnChange##\n$state")},
            onFinished = {state -> log("##OnFinished##\n$state")}
        )
    }
}

fun moveRight(ur: UR) {
    println("\nCMD: arm to the right")
    val joints = ur.jointPosition
    //println("JointPosition:\n" + joints)
    //println("Press Key to send new JointData: ")
    joints?.let {
        ur.arm.movej(
            q = JointPosition(
                base = it.base + Math.toRadians(5.0),
                shoulder = it.shoulder,
                elbow = it.elbow,
                wrist1 = it.wrist1,
                wrist2 = it.wrist2,
                wrist3 = it.wrist3
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0,
            onChange = {state -> log("##OnChange##\n$state")},
            onFinished = {state -> log("##OnFinished##\n$state")}
        )
    }
}

fun moveMultipleTimes1(ur: UR) {
    println("\nCMD: move Multiple Times 1")
    val callback: (ArmState) -> Unit = { armState ->
        ur.arm.movej(
            q = armState.jointPosition.let { it.copy(base = it.base + Math.toRadians(50.0)) },
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0
        )
    }

    ur.arm.movej(
        q = JointPosition(
            base = Math.toRadians(-70.0),
            shoulder = Math.toRadians(-50.0),
            elbow = Math.toRadians(-125.0),
            wrist1 = Math.toRadians(-5.0),
            wrist2 = Math.toRadians(90.0),
            wrist3 = Math.toRadians(5.0)
        ),
        a = 1.2,
        v = 0.9,
        t = 0.0,
        r = 0.0,
        onFinished = callback
    )

}


fun moveMultipleTimes2(ur: UR) {
    println("\nCMD: move Multiple Times 2")

    GlobalScope.launch {
        val armState = ur.arm.movej(
            q = JointPosition(
                base = Math.toRadians(-70.0),
                shoulder = Math.toRadians(-50.0),
                elbow = Math.toRadians(-125.0),
                wrist1 = Math.toRadians(-5.0),
                wrist2 = Math.toRadians(90.0),
                wrist3 = Math.toRadians(5.0)
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0
        ).await()
        println(armState.jointPosition)
        ur.arm.movej(
            q = JointPosition(
                base = Math.toRadians(-20.0),
                shoulder = Math.toRadians(-20.0),
                elbow = Math.toRadians(-145.0),
                wrist1 = Math.toRadians(-5.0),
                wrist2 = Math.toRadians(50.0),
                wrist3 = Math.toRadians(5.0)
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0
        ).await()
    }
}


fun programTest01(ur: UR, tool: OnRobotVG) {
    println("\nCMD: move Multiple Times 2")

    GlobalScope.launch {
        for(i in 0..3) {
            val armState = ur.arm.movej(
                q = JointPosition(
                    base = Math.toRadians(-260.0),
                    shoulder = Math.toRadians(-90.0),
                    elbow = Math.toRadians(-90.0),
                    wrist1 = Math.toRadians(270.0),
                    wrist2 = Math.toRadians(90.0),
                    wrist3 = Math.toRadians(10.0)
                ),
                a = 1.2,
                v = 0.9,
                t = 0.0,
                r = 0.0
            ).await()
            ur.arm.movej(
                q = JointPosition(
                    base = Math.toRadians(-343.0),
                    shoulder = Math.toRadians(-138.0),
                    elbow = Math.toRadians(-81.0),
                    wrist1 = Math.toRadians(309.0),
                    wrist2 = Math.toRadians(91.0),
                    wrist3 = Math.toRadians(19.0)
                ),
                a = 1.2,
                v = 0.9,
                t = 0.0,
                r = 0.0
            ).await()
            tool.release(channel = 2).await()
            tool.seekGrip(channel = 2, vacuum = 50).await()
            ur.arm.movej(
                q = JointPosition(
                    base = Math.toRadians(-343.0),
                    shoulder = Math.toRadians(-138.0),
                    elbow = Math.toRadians(-81.0),
                    wrist1 = Math.toRadians(309.0),
                    wrist2 = Math.toRadians(91.0),
                    wrist3 = Math.toRadians(19.0)
                ),
                a = 1.2,
                v = 0.9,
                t = 0.0,
                r = 0.0
            ).await()
            ur.arm.movej(
                q = JointPosition(
                    base = Math.toRadians(-270.0),
                    shoulder = Math.toRadians(-96.0),
                    elbow = Math.toRadians(-131.0),
                    wrist1 = Math.toRadians(317.0),
                    wrist2 = Math.toRadians(88.0),
                    wrist3 = Math.toRadians(-89.0)
                ),
                a = 1.2,
                v = 0.9,
                t = 0.0,
                r = 0.0
            ).await()
            tool.release(channel = 2).await()
            tool.seekGrip(channel = 2, vacuum = 50).await()
            ur.arm.movej(
                q = JointPosition(
                    base = Math.toRadians(-270.0),
                    shoulder = Math.toRadians(-96.0),
                    elbow = Math.toRadians(-131.0),
                    wrist1 = Math.toRadians(317.0),
                    wrist2 = Math.toRadians(88.0),
                    wrist3 = Math.toRadians(-89.0)
                ),
                a = 1.2,
                v = 0.9,
                t = 0.0,
                r = 0.0
            ).await()
        }
    }
}

fun moveLWithJointPosition(ur: UR) {
    ur.arm.movel(
        q = JointPosition(
            base = Math.toRadians(-270.0),
            shoulder = Math.toRadians(-96.0),
            elbow = Math.toRadians(-131.0),
            wrist1 = Math.toRadians(317.0),
            wrist2 = Math.toRadians(88.0),
            wrist3 = Math.toRadians(-89.0)
        ),
        a = 1.2,
        v = 0.9,
        t = 0.0,
        r = 0.0
    ).awaitBlocking()
}


fun closeGripper2FG7(tool: OnRobotTFG) {
    tool.gripExt(
        width = 0.0,
        force = 40,
        speed = 50,
        onChange = {state -> log("##OnChange##\n$state")},
        onFinished = {state -> log("##OnFinished##\n$state")}
    )
}

fun openGripper2FG7(tool: OnRobotTFG) {
    tool.releaseExt(
        width = 30.0,
        force = 40,
        speed = 50,
        onChange = {state -> log("##OnChange##\n$state")},
        onFinished = {state -> log("##OnFinished##\n$state")}
    )
}

fun closeGripperRG6(tool: OnRobotRG) {
    tool.grip(width = 0.0, force = 40, depthComp = false, popupMsg = false){state ->
        log("##OnFinished##\n$state")
    }
}

fun openGripperRG6(tool: OnRobotRG) {
    tool.release(width = 70.0, force = 40, depthComp = false, popupMsg = false){state ->
        log("##OnFinished##\n$state")
    }
}

fun closeGripperVG(tool: OnRobotVG) {
    tool.grip(channel = 2, vacuum = 60){state ->
        log("##OnFinished##\n$state")
    }
}

fun openGripperVG(tool: OnRobotVG) {
    val state = tool.release(channel = 2){state ->
        log("##OnFinished##\n$state")
    }
}

fun closeGripperVGSeek(tool: OnRobotVG) {
    tool.seekGrip(channel = 2, vacuum = 50){state ->
        log("##OnFinished##\n$state")
    }
}

