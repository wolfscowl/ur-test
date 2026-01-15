package com.wolfscowl.ur_client.examples

import com.wolfscowl.ur_client.UR
import com.wolfscowl.ur_client.examples.Examples.connect
import com.wolfscowl.ur_client.examples.Examples.disconnect
import com.wolfscowl.ur_client.examples.Examples.enterFreeDriveMode
import com.wolfscowl.ur_client.examples.Examples.exitFreeDriveMode
import com.wolfscowl.ur_client.examples.Examples.fetchIsRunning
import com.wolfscowl.ur_client.examples.Examples.fetchLoadedProgram
import com.wolfscowl.ur_client.examples.Examples.fetchPolyscopeVersion
import com.wolfscowl.ur_client.examples.Examples.fetchProgramState
import com.wolfscowl.ur_client.examples.Examples.fetchRobotMode
import com.wolfscowl.ur_client.examples.Examples.fetchRobotModel
import com.wolfscowl.ur_client.examples.Examples.fetchSafetyStatus
import com.wolfscowl.ur_client.examples.Examples.fetchSerialNumber
import com.wolfscowl.ur_client.examples.Examples.load
import com.wolfscowl.ur_client.examples.Examples.movec
import com.wolfscowl.ur_client.examples.Examples.movej
import com.wolfscowl.ur_client.examples.Examples.movel
import com.wolfscowl.ur_client.examples.Examples.pause
import com.wolfscowl.ur_client.examples.Examples.play
import com.wolfscowl.ur_client.examples.Examples.powerOff
import com.wolfscowl.ur_client.examples.Examples.powerOn
import com.wolfscowl.ur_client.examples.Examples.runScript
import com.wolfscowl.ur_client.examples.Examples.setTargetPayload
import com.wolfscowl.ur_client.examples.Examples.testEndEffector
import com.wolfscowl.ur_client.examples.Examples.unlockProtectiveStop
import com.wolfscowl.ur_client.interfaces.state.await
import com.wolfscowl.ur_client.interfaces.tool.OnRobotRG
import com.wolfscowl.ur_client.interfaces.tool.OnRobotTFG
import com.wolfscowl.ur_client.interfaces.tool.OnRobotVG
import com.wolfscowl.ur_client.interfaces.tool.URTool
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.RunningState
import com.wolfscowl.ur_client.model.element.WatchdogConfig
import com.wolfscowl.ur_client.model.robot_state.mode.RobotMode
import com.wolfscowl.ur_client.examples.Examples.setTcpOffset
import com.wolfscowl.ur_client.examples.Examples.stop
import com.wolfscowl.ur_client.model.element.URResult
import com.wolfscowl.ur_client.model.element.Vec3
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.lang.Thread.sleep
import kotlin.reflect.KClass
import kotlin.system.exitProcess


/*
RUN THIS ONLY ON OFFLINE SIMULATION !!!
-> Tested with URSIM UR5
*/
fun main() {
    val urHost = "192.168.56.101"
    val onRobotHost = "192.168.56.102"
    val toolIndex = 0

    runBlocking {

        // === Bring the robot into an operational state ===
        println("\nℹ\uFE0F Trying to connect to $urHost …")
        connect(urHost)

        println("\nℹ\uFE0F Powering on robot …")
        powerOn()


        // ==== Management of *.urp programs via the Dashboard Server ====
        println("\nℹ\uFE0F Managing .urp programs via PolyScope …")
        load("programs/test.urp")
        fetchLoadedProgram()
        fetchIsRunning()
        fetchProgramState()
        play()
        delay(2000)
        pause()
        delay(2000)
        play()
        delay(2000)
        stop()



        // ==== Basic Dashboard Server commands ====
        println("\nℹ\uFE0F Unlocking protective stop …")
        unlockProtectiveStop()

        println("\nℹ\uFE0F Robot model:")
        fetchRobotModel()

        println("\nℹ\uFE0F Serial number:")
        fetchSerialNumber()

        println("\nℹ\uFE0F Polyscope Version:")
        fetchPolyscopeVersion()

        println("\nℹ\uFE0F Robot mode:")
        fetchRobotMode()

        println("\nℹ\uFE0F SafetyStatus:")
        fetchSafetyStatus()



        // ==== Primary Interface URScript commands ====
        println("\nℹ\uFE0F Testing FreeDriveMode …")
        enterFreeDriveMode()
        delay(5000)
        exitFreeDriveMode()

        println("\nℹ\uFE0F Setting TCP offset …")
        setTcpOffset()

        println("\nℹ\uFE0F Setting payload …")
        setTargetPayload()

        println("\nℹ\uFE0F Executing custom script …")
        runScript()

        println("\nℹ\uFE0F Executing MoveJ …")
        movej()

        println("\nℹ\uFE0F Executing MoveC …")
        movec()

        println("\nℹ\uFE0F Executing MoveL …")
        movel()

        println("\nℹ\uFE0F Testing end effector …")
        // tool = OnRobotRG::class || OnRobotVG::class  || OnRobotTFG::class
        testEndEffector(host = onRobotHost, tool = OnRobotRG::class, index = toolIndex)



        // === Shut down and disconnect the robot ===
        println("\nℹ\uFE0F Powering off robot …")
        powerOff()

        //println("\nℹ\uFE0FShutting down robot …")
        //shutdown()

        println("\nℹ\uFE0F Disconnecting …")
        disconnect()
    }
}





object Examples {
    var ur = UR(host = "")



    suspend fun loadInstallation(installation: String){
        val resultDeferred = CompletableDeferred<String>()
        ur.loadInstallation(
            installation = installation,
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)
    }



    suspend fun load(program: String){
        val resultDeferred = CompletableDeferred<String>()
        ur.load(
            program = program,
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)
    }



    suspend fun fetchLoadedProgram(){
        val resultDeferred = CompletableDeferred<String>()
        ur.fetchLoadedProgram(
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)
    }


    suspend fun fetchIsRunning(){
        val resultDeferred = CompletableDeferred<String>()
        ur.fetchIsRunning(
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)
    }



    suspend fun fetchProgramState(){
        val resultDeferred = CompletableDeferred<String>()
        ur.fetchProgramState(
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)
    }



    suspend fun pause(){
        val resultDeferred = CompletableDeferred<String>()
        ur.pause (
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)
    }



    suspend fun stop(){
        val resultDeferred = CompletableDeferred<String>()
        ur.stop(
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)
    }




    suspend fun play(){
        val resultDeferred = CompletableDeferred<String>()
        ur.play (
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)
    }



    suspend fun connect(host: String) {
        ur = UR(host = host, watchdogConfig = WatchdogConfig(enableLogging = false), soTimeout = 5000, logRobotMessages = false)
        try {
            ur.connect()
            ur.isConnectedFlow.first { it }
        } catch (e: Exception) {
            println("Host is unavailable")
            exitProcess(0)
        }
    }



    suspend fun disconnect() {
        ur.disconnect()
        ur.isConnectedFlow.first { !it }
    }



    suspend fun powerOn() {
        val robotMode = ur.robotModeDataFlow
            .map { it?.robotMode }
            .first { it != null }
        if (robotMode == RobotMode.ROBOT_MODE_RUNNING) {
            println("\uD83D\uDFE2 The robot is already powered on.")
            return
        }

        ur.powerOn { println(it) } // "closing popup ... Powering on"

        val success = withTimeoutOrNull(30000) {
            while (ur.robotModeData?.robotMode != RobotMode.ROBOT_MODE_RUNNING) {
                delay(50)
            }
            true
        }

        println(if (success == true) "\uD83D\uDFE2 The robot is turned on." else "\uD83D\uDD34 Something went wrong!")
    }



    suspend fun powerOff() {
        val robotMode = ur.robotModeDataFlow
            .map { it?.robotMode }
            .first { it != null }

        if (robotMode == RobotMode.ROBOT_MODE_POWER_OFF) {
            println("\uD83D\uDFE2 The robot is already powered off.")
            return
        }

        ur.powerOff { println(it) } // "Powering off"

        val success = withTimeoutOrNull(30000) {
            while (ur.robotModeData?.robotMode != RobotMode.ROBOT_MODE_POWER_OFF) {
                delay(50)
            }
            true
        }

        println(if (success == true) "\uD83D\uDFE2 The robot is powered off." else "\uD83D\uDD34 Something went wrong!")
    }



    suspend fun fetchRobotModel(){
        val resultDeferred = CompletableDeferred<String>()
        ur.fetchRobotModel { result ->
            resultDeferred.complete(result)
        }
        val result = resultDeferred.await()
        println(result) // e.g. "UR5"
    }



    suspend fun fetchRobotMode(){
        val resultDeferred = CompletableDeferred<String>()
        ur.fetchRobotMode(
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result) // e.g. "Robotmode: RUNNING"
    }



    suspend fun unlockProtectiveStop(){
        val protectiveStop = ur.robotModeDataFlow
            .first { it != null }!!
            .let { it.securityStopped || it.emergencyStopped || it.programPaused }
        if (!protectiveStop) {
            println("\uD83D\uDFE2 No security or emergency stop recognized.")
            return
        }

        val resultDeferred = CompletableDeferred<String>()
        ur.unlockProtectiveStop(
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)  // e.g. "Protective stop releasing"
        delay(150)  // Gives the primary interface (10 Hz) enough time to update the state ur.robotModeData
    }



    suspend fun fetchSafetyStatus(){
        val resultDeferred = CompletableDeferred<String>()
        ur.fetchSafetyStatus(
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)  //  e.g. "Safetystatus: NORMAL"
    }



    suspend fun fetchSerialNumber(){
        val resultDeferred = CompletableDeferred<String>()
        ur.fetchSerialNumber(
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result)
    }



    suspend fun shutdown(){
        val resultDeferred = CompletableDeferred<String>()
        ur.shutdown(
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result) // "Shutting down"
    }



    suspend fun fetchPolyscopeVersion(){
        val resultDeferred = CompletableDeferred<String>()
        ur.fetchPolyscopeVersion(
            onResponse = { resultDeferred.complete(it) },
            onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
        )
        val result = resultDeferred.await()
        println(result) // e.g. URSoftware 5.12.6.1102099 (Sep 21 2023)
    }



    fun enterFreeDriveMode() {
        ur.arm.enterFreeDriveMode(
            t = 20.0f,
            cmdTimeout = 30000,
            onChange = { state -> if (state.runningState == RunningState.START) println("FreeDrive mode is now running...") },
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                    println("\uD83D\uDFE2 FreeDrive mode was successfully activated and automatically stopped after 20 seconds.")
                else if (state.runningState == RunningState.CANCELED)
                    println("\uD83D\uDFE1 FreeDrive mode was stopped prematurely.")
                else
                    println("\uD83D\uDD34 Something went wrong. See state below.\n $state")
            }
        )
    }



    suspend fun exitFreeDriveMode() {
        ur.arm.exitFreeDriveMode(
            cmdTimeout = 3000,
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                    println("\uD83D\uDFE2 FreeDrive mode was successfully stopped.")
                else
                    println("\uD83D\uDD34 Something went wrong. See state below.\n $state")
            }
        ).await()   // await for the finale state
    }



    suspend fun setTcpOffset() {
        val state = ur.arm.setTcpOffset(
            p = Pose(0.00,0.00,0.00,0.00,0.00,0.00),
            cmdTimeout = 5000,
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                    println("\uD83D\uDFE2 The TCP offset was successfully set.")
                else
                    println("\uD83D\uDD34 Something went wrong. See state below.")
            }
        ).await()  // await for the finale state
        println(state)
    }



    suspend fun setTargetPayload() {
        val state = ur.arm.setTargetPayload(
            m = 0.26f,
            cog = Vec3(0.00,0.00,0.00),
            inertia = null, // null corresponds to a sphere with 1g/cm3.
            cmdTimeout = 5000,
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                    println("\uD83D\uDFE2 The payload was successfully set.")
                else
                    println("\uD83D\uDD34 Something went wrong. See state below.")
            }
        ).await()  // await for the finale state
        println(state)
    }



    suspend fun runScript() {
        val state = ur.runURScript(
            script = """
            def base_loop():
              i = 0
              delta = 0.4
              while (i < 3):
                movej([-1.57 + delta, -1.57, 0.00, -3.14, -1.57, -1.57], a=1.4, v=1.05, t=0, r=0)
                movej([-1.57, -1.57, 0.00, -3.14, -1.57, -1.57], a=1.4, v=1.05, t=0, r=0)
                i = i + 1
              end
            end
            
            send_event("error", "Test Error", "This is just a test :)")
            
            base_loop()
            """.trimIndent(),
            cmdTimeout = 20000,
            onChange = { },
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                    println("\uD83D\uDFE2 The custom script was executed successfully.")
                else
                    println("\uD83D\uDD34 Something went wrong. See state below.")
            }
        ).await()  // await for the finale state
        println(state)
    }



    suspend fun movej() {
        val state = ur.arm.movej(
            q = JointPosition(
                base = Math.toRadians(90.00),
                shoulder = Math.toRadians(-90.00),
                elbow = Math.toRadians(90.00),
                wrist1 = Math.toRadians(-90.00),
                wrist2 = Math.toRadians(-90.00),
                wrist3 = Math.toRadians(-90.00)
            ),
            a = 1.2,
            v = 0.9,
            t = 0.0,
            r = 0.0,
            cmdTimeout = 20000,
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                    println("\uD83D\uDFE2 The movej command was executed successfully.")
                else
                    println("\uD83D\uDD34 Something went wrong. See state below.")
            }
        ).await()  // await for the finale state
        println(state)
    }



    suspend fun movec() {
        val state = ur.arm.movec(
            poseTo = Pose(
                x = 0.1332,
                y = -0.5309,
                z =  0.2484,
                rx = -2.2214,
                ry = 2.2214,
                rz = -0.000
            ),
            poseVia = Pose(
                x = 0.1333,
                y = -0.7043,
                z = 0.4309,
                rx = 2.316,
                ry = -2.122,
                rz = -0.000
            ),
            a = 0.8,
            v = 0.2,
            cmdTimeout = 20000,
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                    println("\uD83D\uDFE2 The movec command was executed successfully.")
                else
                    println("\uD83D\uDD34 Something went wrong. See state below.")
            }
        ).await()  // await for the finale state
        println(state)
    }



    fun movel() {
        runBlocking {
            val state = ur.arm.movel(
                p = Pose(
                    x = 0.52434,
                    y = 0.17672,
                    z = 0.50086,
                    rx = 2.299,
                    ry = 0.000, // 0.230
                    rz = 2.094
                ),
                a = 0.8,
                v = 0.2,
                t = 0.0,
                r = 0.0,
                cmdTimeout = 10000,
                onFinished = { state ->
                    if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                        println("\uD83D\uDFE2 The movel command was executed successfully.")
                    else
                        println("\uD83D\uDD34 Something went wrong. See state below.")
                }
            ).await()  // await for the finale state
            println(state)
        }
    }



    suspend fun testEndEffector(host: String, index: Int, tool: KClass<out URTool>) {
         when (tool) {
             OnRobotRG::class -> testOnRobotRG(host, index)
             OnRobotTFG::class -> testOnRobotTFG(host, index)
             OnRobotVG::class -> testOnRobotVG(host, index)
        }
    }



    suspend fun testOnRobotRG(host: String, index: Int) {
        val rg = ur.attachToolOnRobotRG(host = host, toolIndex = index)
        val state = rg.grip(
            width = 0.0,
            force = 100,
            depthComp = true,
            cmdTimeout = 10000,
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                    println("\uD83D\uDFE2 The RG grip command was executed successfully.")
                else
                    println("\uD83D\uDD34 Something went wrong. See state below.")
            }
        ).await()  // await for the finale state
        println(state)
    }



    suspend fun testOnRobotTFG(host: String, index: Int) {
        val tfg = ur.attachToolOnRobotTFG(host = host, toolIndex = index)
        val state = tfg.gripExt(
            width = 0.0,
            force = 100,
            speed = 100,
            cmdTimeout = 10000,
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                    println("\uD83D\uDFE2 The TFG grip command was executed successfully.")
                else
                    println("\uD83D\uDD34 Something went wrong. See state below.")
            }
        ).await()  // await for the finale state
        println(state)
    }



    suspend fun testOnRobotVG(host: String, index: Int) {
        val tfg = ur.attachToolOnRobotVG(host = host, toolIndex = index)
        val state = tfg.grip(
            channel = 2,
            vacuum = 80,
            cmdTimeout = 10000,
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred && !state.safetyModeStop)
                    println("\uD83D\uDFE2 The VG grip command was executed successfully.")
                else
                    println("\uD83D\uDD34 Something went wrong. See state below.")
            }
        ).await()  // await for the finale state
        println(state)
    }



}