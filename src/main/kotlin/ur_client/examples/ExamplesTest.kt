package com.wolfscowl.ur_client.examples

import com.wolfscowl.ur_client.UR
import com.wolfscowl.ur_client.examples.Examples.connect
import com.wolfscowl.ur_client.examples.Examples.fetchIsRunning
import com.wolfscowl.ur_client.examples.Examples.fetchLoadedProgram
import com.wolfscowl.ur_client.examples.Examples.fetchProgramState
import com.wolfscowl.ur_client.examples.Examples.load
import com.wolfscowl.ur_client.examples.Examples.loadInstallation
import com.wolfscowl.ur_client.examples.Examples.pause
import com.wolfscowl.ur_client.examples.Examples.play
import com.wolfscowl.ur_client.examples.Examples.powerOn
import com.wolfscowl.ur_client.examples.Examples.runScript
import com.wolfscowl.ur_client.examples.Examples.stop
import com.wolfscowl.ur_client.examples.Examples.ur
import com.wolfscowl.ur_client.interfaces.state.await
import com.wolfscowl.ur_client.interfaces.state.awaitBlocking
import com.wolfscowl.ur_client.interfaces.state.awaitBlockingUntil
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.RunningState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep


fun threadAwait() {
    val ur = UR("192.168.2.1")
    ur.connect()
    if (ur.isConnected) {
        ur.arm.enterFreeDriveMode(
            t = 20.0f,
            cmdTimeout = 30000,
            onChange = null,
            onFinished = null
        ).awaitBlocking()
    }
}

fun main() {
    val ur = UR("192.168.178.32")
    ur.connect()
    if (ur.isConnected) {
        ur.arm.enterFreeDriveMode(
            t = 20.0f,
            cmdTimeout = 30000,
            onChange = null,
            onFinished = null
        ).awaitBlockingUntil { it.runningState == RunningState.START }
    }
}



// JOINTS anfahren
fun main8() {
    val ur = UR("192.168.178.32")

    runBlocking {
        ur.connect()
        ur.isConnectedFlow.first { it }

        val state = ur.arm.movej(
            q = JointPosition(
                base = Math.toRadians(-90.00),
                shoulder = Math.toRadians(-125.00),
                elbow = Math.toRadians(-80.00),
                wrist1 = Math.toRadians(-65.00),
                wrist2 = Math.toRadians(90.00),
                wrist3 = Math.toRadians(-90.00)
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0,
            cmdTimeout = 20000,
            onChange = null,
            onFinished = { state -> /* Do something */ }
        ).await() // await for the finale state
        println(state)
    }
}


// TCP ANFAHREN
fun main22() {
    val ur = UR("192.168.178.32")

    runBlocking {
        ur.connect()
        ur.isConnectedFlow.first { it }

        val state = ur.arm.movel(
            p =  Pose(
                x = -0.133,
                y = -0.704,
                z = 0.2484,
                rx = -2.2214,
                ry = 2.2214,
                rz = -0.000
            ),
            a = 0.8,
            v = 0.2,
            t = 0.0,
            r = 0.0,
            cmdTimeout = 100000,
            onChange = null,
            onFinished = { state -> /* Do something */ }
        ).await() // await for the finale state
        println(state)
    }
}




// MOVEC Pose
fun main45t62() {
    val ur = UR("192.168.178.32")

    runBlocking {
        ur.connect()
        ur.isConnectedFlow.first { it }

        val state = ur.arm.movec(
            poseVia = Pose(
                x = -0.133,
                y = -0.704,
                z = 0.430,
                rx = -1.80,
                ry = 2.57,
                rz = 0.000
            ),
            poseTo = Pose(
                x = -0.133,
                y = -0.698,
                z = 0.245,
                rx = 2.221,
                ry = -2.221,
                rz = 0.000
            ),
            a = 0.8,
            v = 0.2,
            cmdTimeout = 20000,
            onChange = null,
            onFinished = { state -> /* Do something */ }
        ).await() // await for the finale state
        println(state)
    }
}


// MOVEC Joionts
fun main333() {
    val ur = UR("192.168.178.32")

    runBlocking {
        ur.connect()
        ur.isConnectedFlow.first { it }

        val state = ur.arm.movec(
            positionVia = JointPosition(
                base = Math.toRadians(-90.00),
                shoulder = Math.toRadians(-120.00),
                elbow = Math.toRadians(-60.00),
                wrist1 = Math.toRadians(-90.00),
                wrist2 = Math.toRadians(90.00),
                wrist3 = Math.toRadians(-70.00)
            ),
            positionTo = JointPosition(
                base = Math.toRadians(-90.00),
                shoulder = Math.toRadians(-125.00),
                elbow = Math.toRadians(-80.00),
                wrist1 = Math.toRadians(-65.00),
                wrist2 = Math.toRadians(90.00),
                wrist3 = Math.toRadians(-90.00)
            ),
            a = 0.8,
            v = 0.2,
            cmdTimeout = 20000,
            onChange = null,
            onFinished = { state -> /* Do something */ }
        ).await() // await for the finale state
        println(state)
    }
}


fun main334() {
    val ur = UR("192.168.178.32")

    runBlocking {
        ur.connect()
        ur.isConnectedFlow.first { it }

        val state = ur.arm.movel(
            p =  Pose(
                x = -0.133,
                y = -0.704,
                z = 0.2484,
                rx = -2.2214,
                ry = 2.2214,
                rz = -0.000
            ),
            a = 0.8,
            v = 0.2,
            t = 0.0,
            r = 0.0,
            cmdTimeout = 100000,
            onChange = null,
            onFinished = { state -> /* Do something */ }
        ).await() // await for the finale state
        println(state)
    }
}



fun main23() {
    val ur = UR("192.168.178.32")

    runBlocking {
        ur.connect()
        ur.isConnectedFlow.first { it }

        val state = ur.arm.movel(
            q = JointPosition(
                base = Math.toRadians(180.00),
                shoulder = Math.toRadians(-70.00),
                elbow = Math.toRadians(110.00),
                wrist1 = Math.toRadians(-220.00),
                wrist2 = Math.toRadians(-90.00),
                wrist3 = Math.toRadians(-90.00)
            ),
            a = 0.8,
            v = 0.2,
            t = 0.0,
            r = 0.0,
            cmdTimeout = 100000,
            onChange = null,
            onFinished = { state -> /* Do something */ }
        ).await() // await for the finale state
        println(state)
    }
}






fun main34e4() {

        val ur = UR("192.168.178.32")

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            ur.arm.enterFreeDriveMode(
                t = 20.0f,
                cmdTimeout = 30000,
                onChange = {  },
                onFinished = { state -> /* Do something */ }
            )
            ur.additionalInfoFlow.first{ it?.freedriveButtonEnabled == true }
            /* Do something with the robot arm */
            delay(10000)
            ur.arm.exitFreeDriveMode(
                cmdTimeout = 30000,
                onChange = {  },
                onFinished = { state -> /* Do something */ }
            ).await()
        }

}




fun main2() {
    val urHost = "192.168.178.32"
    val onRobotHost = "192.168.56.102"
    val toolIndex = 0

    runBlocking {

        // === Bring the robot into an operational state ===
        println("\nℹ\uFE0F Trying to connect to $urHost …")
        connect(urHost)

        println("\nℹ\uFE0F Powering on robot …")
        powerOn()


        println("\nℹ\uFE0F Managing .urp programs via PolyScope …")
        fetchLoadedProgram()
        runScript()
        delay(500)
        fetchIsRunning()
        fetchProgramState()
        delay(5000)
        stop()

        // ==== Management of *.urp programs via the Dashboard Server ====
//        println("\nℹ\uFE0F Managing .urp programs via PolyScope …")
//        loadInstallation("installations/default.installations")
//        load("programs/test.urp")
//        fetchLoadedProgram()
//        //powerOn()
//        play()
//        delay(1000)
//        fetchIsRunning()
//        pause()
//        fetchProgramState()
//        delay(2000)
//        play()
//        delay(2000)
//        stop()
    }


}