package com.wolfscowl.ur_client.examples

import com.wolfscowl.ur_client.UR
import com.wolfscowl.ur_client.examples.Examples.ur
import com.wolfscowl.ur_client.interfaces.state.await
import com.wolfscowl.ur_client.interfaces.state.awaitBlocking
import com.wolfscowl.ur_client.interfaces.state.awaitBlockingUntil
import com.wolfscowl.ur_client.interfaces.state.awaitUntil
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.RunningState
import com.wolfscowl.ur_client.model.element.Vec3
import com.wolfscowl.ur_client.model.robot_state.mode.RobotMode
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess


object ExamplesDoc {

    // ===================================== DASHBOARD SERVER ======================================


    fun powering() {
        val ur = UR("192.168.2.1")

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }

            ur.powerOn { println(it) }
            ur.robotModeDataFlow.first{ it?.robotMode ==  RobotMode.ROBOT_MODE_RUNNING}

            /* Do something */

            ur.powerOff { println(it) }
            ur.robotModeDataFlow.first{ it?.robotMode ==  RobotMode.ROBOT_MODE_POWER_OFF}
        }
    }


    fun urpLoad() {
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.load(
                program = "programs/test.urp",
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun urpPlay() {
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.play(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun urpStop() {
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.stop(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun urpPause() {
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.pause(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun urpLoaded() {
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.fetchLoadedProgram(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun urpProgramState() {
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.fetchProgramState(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun urpIsRunning() {
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.fetchIsRunning(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun urpInstallation(){
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.loadInstallation(
                installation = "installations/default.installations",
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun robotModel(){
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.fetchRobotModel(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun robotMode(){
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.fetchRobotMode(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun polyscopeVersion(){
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.fetchPolyscopeVersion(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun serialNumber(){
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.fetchSerialNumber(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun safetyStatus(){
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.fetchSafetyStatus(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun unlockProtectiveStop(){
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.unlockProtectiveStop(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    fun shutdown(){
        val ur = UR("192.168.2.1")

        runBlocking {
            val resultDeferred = CompletableDeferred<String>()
            ur.shutdown(
                onResponse = { resultDeferred.complete(it) },
                onFailure = { resultDeferred.complete(it.message ?: "Something went wrong") }
            )
            val result = resultDeferred.await()
            println(result)
        }
    }


    // ============================== PRIMARY INTERFACE - FUNDAMENTAL ==============================


    fun connection() {
        val ur = UR("192.168.2.1")

        runBlocking {
            try {
                ur.connect()
                ur.isConnectedFlow.first { it }
            } catch(e: Exception) {
                println ("Host is unavailable")
                exitProcess(1)
            }
            /* Do something */
            ur.disconnect()
            ur.isConnectedFlow.first { !it }
        }
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
            
            send_event("error", "Test Error", "This is just a test ;)")
            
            base_loop()
            """.trimIndent(),
            cmdTimeout = 20000,
            onChange = null,
            onFinished = { state -> /* Do something */
            }
        ).await() // await for the finale state
        println(state)
    }


    fun freeDriveMode() {
        val ur = UR("192.168.2.1")

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            ur.arm.enterFreeDriveMode(
                t = 20.0f,
                cmdTimeout = 30000,
                onChange = null,
                onFinished = { state -> /* Do something */ }
            )
            ur.additionalInfoFlow.first{ it?.freedriveButtonEnabled == true }
            /* Do something with the robot arm */
            delay(10000)
            ur.arm.exitFreeDriveMode(
                cmdTimeout = 5000,
                onChange = null,
                onFinished = { state -> /* Do something */ }
            ).await() // await for the finale state
        }
    }


    fun setTcpOffset() {
        val ur = UR("192.168.2.1")

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }

            val state = ur.arm.setTcpOffset(
                p = Pose(0.00, 0.00, 0.05, 0.00, 0.00, 0.00),
                cmdTimeout = 5000,
                onChange = null,
                onFinished = { state -> /* Do something */ }
            ).await() // await for the finale state
            println(state)
        }
    }


    fun setTargetPayload() {
        val ur = UR("192.168.2.1")

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            val state = ur.arm.setTargetPayload(
                m = 0.20f,
                cog = Vec3(0.00, 0.00, 0.00),
                inertia = null, // null corresponds to a sphere with 1g/cm3.
                cmdTimeout = 5000,
                onChange = null,
                onFinished = { state -> /* Do something */ }
            ).await() // await for the finale state
            println(state)
        }
    }


    fun movejJoint() {
        val ur = UR("192.168.2.1")

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }

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
                onChange = null,
                onFinished = { state -> /* Do something */ }
            ).await() // await for the finale state
            println(state)
        }
    }


    fun movejTCP() {
        val ur = UR("192.168.2.1")

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }

            val state = ur.arm.movej(
                p = Pose(
                    x = 0.133,
                    y = -0.491,
                    z = 0.487,
                    rx = -2.22,
                    ry = 2.22,
                    rz = 0.0,
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


    fun movelTCP() {
        val ur = UR("192.168.2.1")

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }

            val state = ur.arm.movel(
                p = Pose(
                    x = 0.545,
                    y = 0.133,
                    z = 0.409,
                    rx = 2.221,
                    ry = 0.000,
                    rz = 2.221
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


    fun movelJoint() {
        val ur = UR("192.168.2.1")

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }

            val state = ur.arm.movel(
                q = JointPosition(
                    base = Math.toRadians(180.00),
                    shoulder = Math.toRadians(-90.00),
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


    fun movecTCP() {
        val ur = UR("192.168.2.1")

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


    fun movecJoint() {
        val ur = UR("192.168.2.1")

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


    // ============================= PRIMARY INTERFACE - END EFFECTOR ==============================


    fun onRobotRGGrip() {
        val ur = UR("192.168.2.1")
        val rg = ur.attachToolOnRobotRG(host = "192.168.2.2", toolIndex = 0)

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            val state = rg.grip(
                width = 0.0,
                force = 100,
                depthComp = true,
                cmdTimeout = 10000,
                popupMsg = false,
                onChange = null,
                onFinished = { state -> /* do something */}
            ).await() // await for the finale state
            println(state)
        }
    }


    fun onRobotRGRelease() {
        val ur = UR("192.168.2.1")
        val rg = ur.attachToolOnRobotRG(host = "192.168.2.2", toolIndex = 0)

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            val state = rg.release(
                width = 20.0,
                force = 100,
                depthComp = true,
                cmdTimeout = 5000,
                popupMsg = false,
                onChange = null,
                onFinished = { state -> /* do something */}
            ).await() // await for the finale state
            println(state)
        }
    }


    fun onRobotTFGGripExt() {
        val ur = UR("192.168.2.1")
        val tfg = Examples.ur.attachToolOnRobotTFG(host = "192.168.2.2", toolIndex = 0)

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            val state = tfg.gripExt(
                width = 0.0,
                force = 100,
                speed = 100,
                cmdTimeout = 5000,
                popupMsg = false,
                onChange = null,
                onFinished = { state -> /* do something */}
            ).await() // await for the finale state
            println(state)
        }
    }


    fun onRobotTFGReleaseExt() {
        val ur = UR("192.168.2.1")
        val tfg = Examples.ur.attachToolOnRobotTFG(host = "192.168.2.2", toolIndex = 0)

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            val state = tfg.releaseExt(
                width = 20.0,
                force = 100,
                speed = 100,
                cmdTimeout = 5000,
                popupMsg = false,
                onChange = null,
                onFinished = { state -> /* do something */}
            ).await() // await for the finale state
            println(state)
        }
    }


    fun onRobotTFGGripInt() {
        val ur = UR("192.168.2.1")
        val tfg = Examples.ur.attachToolOnRobotTFG(host = "192.168.2.2", toolIndex = 0)

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            val state = tfg.gripInt(
                width = 20.0,
                force = 100,
                speed = 100,
                cmdTimeout = 5000,
                popupMsg = false,
                onChange = null,
                onFinished = { state -> /* do something */}
            ).await() // await for the finale state
            println(state)
        }
    }


    fun onRobotTFGReleaseInt() {
        val ur = UR("192.168.2.1")
        val tfg = Examples.ur.attachToolOnRobotTFG(host = "192.168.2.2", toolIndex = 0)

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            val state = tfg.releaseExt(
                width = 0.0,
                force = 100,
                speed = 100,
                cmdTimeout = 5000,
                popupMsg = false,
                onChange = null,
                onFinished = { state -> /* do something */}
            ).await() // await for the finale state
            println(state)
        }
    }


    fun onRobotVGGrip() {
        val ur = UR("192.168.2.1")
        val vg = ur.attachToolOnRobotVG(host = "192.168.2.2", toolIndex = 0)

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            val state = vg.grip(
                channel = 2,
                vacuum = 80,
                gripTimeout = 2.0f,
                cmdTimeout = 5000,
                popupMsg = false,
                onChange = null,
                onFinished = { state -> /* do something */}
            ).await() // await for the finale state
            println(state)
        }
    }


    fun onRobotVGRelease() {
        val ur = UR("192.168.2.1")
        val vg = ur.attachToolOnRobotVG(host = "192.168.2.2", toolIndex = 0)

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            val state = vg.release(
                channel = 2,
                releaseTimeout = 2.0f,
                cmdTimeout = 5000,
                popupMsg = false,
                onChange = null,
                onFinished = { state -> /* do something */}
            ).await() // await for the finale state
            println(state)
        }
    }


    fun onRobotVGSeekGrip() {
        val ur = UR("192.168.2.1")
        val vg = ur.attachToolOnRobotVG(host = "192.168.2.2", toolIndex = 0)

        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            val state = vg.seekGrip(
                channel = 2,
                vacuum = 80,
                gripTimeout = 2.0f,
                cmdTimeout = 5000,
                popupMsg = false,
                onChange = null,
                onFinished = { state -> /* do something */}
            ).await() // await for the finale state
            println(state)
        }
    }


    // ================================= PRIMARY INTERFACE - AWAIT =================================


    fun coroutineAwait() {
        val ur = UR("192.168.2.1")
        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            ur.arm.enterFreeDriveMode(
                t = 20.0f,
                cmdTimeout = 30000,
                onChange = null,
                onFinished = null
            ).await()
        }
    }


    fun coroutineAwaitUntil() {
        val ur = UR("192.168.2.1")
        runBlocking {
            ur.connect()
            ur.isConnectedFlow.first { it }
            ur.arm.enterFreeDriveMode(
                t = 20.0f,
                cmdTimeout = 30000,
                onChange = null,
                onFinished = null
            ).awaitUntil { it.runningState == RunningState.START }
        }
    }


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


    fun threadAwaitUntil() {
        val ur = UR("192.168.2.1")
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




}