package com.wolfscowl.ur_client.test

import com.wolfscowl.ur_client.UR
import com.wolfscowl.ur_client.core.internal.cmd_handler.CmdHandler.job
import com.wolfscowl.ur_client.core.internal.util.Util.log
import com.wolfscowl.ur_client.interfaces.state.awaitBlocking
import com.wolfscowl.ur_client.interfaces.state.awaitBlockingUntil
import com.wolfscowl.ur_client.model.element.RunningState
import com.wolfscowl.ur_client.model.robot_message.KeyMessage
import com.wolfscowl.ur_client.model.robot_message.RobotMessage
import com.wolfscowl.ur_client.model.robot_message.TextMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.awt.SystemColor.text
import kotlin.system.measureTimeMillis

fun main() {
    val urHost = "192.168.178.32"
    //val urHost = "192.168.12.145"
    //val urHost = "139.6.77.157"
    //val urHost = "192.168.56.101"
    //val urHost = "192.168.1.15"
    //val urHost = "192.168.56.101"
    val onRobotHost = "192.168.12.146"

    val ur = UR(host = urHost, interfacePort = 30001, logRobotMessages = false, logRobotStates = false)
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
            println("1. Measure - StateFrequencyDuration")
            println("2. Measure - MessageFrequencyDuration")
            println("3. Measure - FirstScriptStateChangeDuration")
            println("4. Measure - FirstScriptStateChangeDurationLong")
            println("5. Measure - ProgramSingleScriptDuration")
            println("6. Measure - ProgramMultiScriptDuration")
            println("0. Exit")
            val input = readLine()
            when (input) {
                "a" -> powerCobotOn(ur)
                "b" -> powerCobotOff(ur)
                "1" -> stateFrequencyDuration(ur)
                "2" -> messageFrequencyDuration(ur)
                "3" -> firstScriptStateChangeDuration(ur)
                "4" -> firstScriptStateChangeDurationLong(ur)
                "5" -> programSingleScriptDuration(ur)
                "6" -> programMultiScriptDuration(ur)
                "0" -> break
            }
        }
    }

}


fun powerCobotOn(ur: UR) {
    ur.powerOn {  test -> println(test) }
}

fun powerCobotOff(ur: UR) {
    ur.powerOff()
}

fun stateFrequencyDuration(ur: UR) {
    var lastTime = System.currentTimeMillis()

    ur.jointDataFlow
        .take(10)
        .onEach { data ->
            val now = System.currentTimeMillis()
            val duration = now - lastTime
            lastTime = now

            println("Zeit seit letzte Emission: $duration ms")
        }
        .launchIn(CoroutineScope(Dispatchers.IO))
}


fun messageFrequencyDuration(ur: UR) {
    val script = """
        sleep(3.)
        send_event("error","BadValue","Fix it")
        send_event("error","BadValue","Fix it")
        send_event("error","BadValue","Fix it")
        send_event("error","BadValue","Fix it")
        send_event("error","BadValue","Fix it")
        send_event("error","BadValue","Fix it")
        send_event("error","BadValue","Fix it")
        send_event("error","BadValue","Fix it")
        send_event("error","BadValue","Fix it")
        send_event("error","BadValue","Fix it")
        send_event("error","BadValue","Fix it")
    """.trimIndent()

    ur.runURScript(
        script = script,
        cmdTimeout = 1000L,
        onChange = { },
        onFinished = {}
    )

    var lastTime = System.currentTimeMillis()

    ur.robotMessagesFlow
        .filterIsInstance<TextMessage>()
        .take(10)
        .onEach { data ->
            val now = System.currentTimeMillis()
            val duration = now - lastTime
            lastTime = now
            println("Zeit seit letzten Emission: $duration ms")
        }
        .launchIn(CoroutineScope(Dispatchers.IO))
}




fun firstScriptStateChangeDuration(ur: UR) {
    val script = """
        send_event("error","BadValue","Fix it")
    """.trimIndent()

    var lastTime = System.currentTimeMillis()
    val job = ur.robotMessagesFlow
        .onEach {
            if (it is KeyMessage || it is TextMessage) {
                val now = System.currentTimeMillis()
                val duration = now - lastTime
                lastTime = now
                println("\nZeit seit letzter Emission: $duration ms")
                println(it)
            }
        }
        .launchIn(CoroutineScope(Dispatchers.IO))

    val duration = measureTimeMillis {
        val state = ur.runURScript(
            script = script,
            cmdTimeout = 10000L,
            onChange = { },
            onFinished = {}
        ).awaitBlockingUntil { it.runningState == RunningState.START }
    }
    println("\nZeit bis zum Programmstart: $duration ms")
    job.cancel()
}


fun firstScriptStateChangeDurationLongOther(ur: UR) {
    val script = """

            payload = get_target_payload()
            cog = get_target_payload_cog()
            inertia = get_target_payload_inertia()
            offset = get_tcp_offset()
            
            send_event("payload", payload)
            send_event("payload_cog", cog[0], cog[1], cog[2])
            send_event("payload_inertia", inertia[0], inertia[1], inertia[2], inertia[3], inertia[4], inertia[5])
            send_event("tcp_offset", offset[0], offset[1], offset[2], offset[3], offset[4], offset[5])
            
            movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            movej([${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            
            payload = get_target_payload()
            cog = get_target_payload_cog()
            inertia = get_target_payload_inertia()
            offset = get_tcp_offset()
            
            send_event("payload", payload)
            send_event("payload_cog", cog[0], cog[1], cog[2])
            send_event("payload_inertia", inertia[0], inertia[1], inertia[2], inertia[3], inertia[4], inertia[5])
            send_event("tcp_offset", offset[0], offset[1], offset[2], offset[3], offset[4], offset[5])

    """.trimIndent()

    var lastTime = System.currentTimeMillis()
    val job = ur.robotMessagesFlow
        .onEach {
            if (it is KeyMessage || it is TextMessage) {
                val now = System.currentTimeMillis()
                val duration = now - lastTime
                lastTime = now
                println("\nZeit seit letzter Emission: $duration ms")
                println(it)
            }
        }
        .launchIn(CoroutineScope(Dispatchers.IO))
    val duration = measureTimeMillis {
        val state = ur.runURScript(
            script = script,
            cmdTimeout = 10000L,
            onChange = { },
            onFinished = {}
        ).awaitBlockingUntil { it.runningState == RunningState.START }
    }
    println("\nZeit bis zum Programmstart: $duration ms")
    job.cancel()
}





fun firstScriptStateChangeDurationLong(ur: UR) {
    val script = """
            movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            movej([${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
             movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            movej([${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
             movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            movej([${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
             movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            movej([${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
             movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            movej([${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
             movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            movej([${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
             movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            movej([${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(20.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
             movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
    """.trimIndent()

    var lastTime = System.currentTimeMillis()
    val job = ur.robotMessagesFlow
        .onEach {
            if (it is KeyMessage || it is TextMessage) {
                val now = System.currentTimeMillis()
                val duration = now - lastTime
                lastTime = now
                println("\nZeit seit letzter Emission: $duration ms")
                println(it)
            }
        }
        .launchIn(CoroutineScope(Dispatchers.IO))
    val duration = measureTimeMillis {
        val state = ur.runURScript(
            script = script,
            cmdTimeout = 10000L,
            onChange = { },
            onFinished = {}
        ).awaitBlockingUntil { it.runningState == RunningState.START }
    }
    println("\nZeit bis zum Programmstart: $duration ms")
    job.cancel()
}

fun programSingleScriptDuration(ur: UR) {
    for(i in 0 .. 20) {
        moveToStartPosition(ur)
        val script = """
            movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${
            Math.toRadians(
                -90.0
            )
        },${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            movej([${Math.toRadians(10.0)},${Math.toRadians(-70.0)},${Math.toRadians(10.0)},${
            Math.toRadians(
                -90.0
            )
        },${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            movej([${Math.toRadians(10.0)},${Math.toRadians(-50.0)},${Math.toRadians(10.0)},${
            Math.toRadians(
                -90.0
            )
        },${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
        """.trimIndent()
        val duration = measureTimeMillis {
            ur.runURScript(
                script = script,
                cmdTimeout = 10000L,
            ).awaitBlocking()
        }
        logProgramDuration("single", duration)
        println("\nZeit bis zur Programmausführung: $duration ms")
    }
}


fun programMultiScriptDuration(ur: UR) {
    for(i in 0 .. 50) {
        moveToStartPosition(ur)
        val script1 = """
            movej([${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(10.0)},${
            Math.toRadians(
                -90.0
            )
        },${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
        """.trimIndent()
        val script2 = """
            movej([${Math.toRadians(10.0)},${Math.toRadians(-70.0)},${Math.toRadians(10.0)},${
            Math.toRadians(
                -90.0
            )
        },${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
    """.trimIndent()
        val script3 = """
            movej([${Math.toRadians(10.0)},${Math.toRadians(-50.0)},${Math.toRadians(10.0)},${
            Math.toRadians(
                -90.0
            )
        },${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
    """.trimIndent()
        val duration1 = measureTimeMillis {
            ur.runURScript(
                script = script1,
                cmdTimeout = 10000L,
            ).awaitBlocking()
        }
        val duration2 = measureTimeMillis {
            ur.runURScript(
                script = script2,
                cmdTimeout = 10000L,
            ).awaitBlocking()
        }
        val duration3 = measureTimeMillis {
            ur.runURScript(
                script = script3,
                cmdTimeout = 10000L,
            ).awaitBlocking()
        }
        val durationTotal = duration1 + duration2 + duration3
        println("\nZeit der Programmausführung: $durationTotal ms")
        println("Step1: ${duration1} ms")
        println("Step2: ${duration2} ms")
        println("Step3: ${duration3} ms")
        logProgramDuration("multi", durationTotal, duration1, duration2, duration3)
    }
}




fun moveToStartPosition(ur: UR) {
    val script = """
            movej([${Math.toRadians(10.0)},${Math.toRadians(-110.0)},${Math.toRadians(10.0)},${Math.toRadians(-90.0)},${Math.toRadians(0.0)},${Math.toRadians(0.0)}], a=1.2, v=0.9,t=0, r=0)
            sleep(1.0)
    """.trimIndent()
    ur.runURScript(
        script = script,
        cmdTimeout = 10000L,
    ).awaitBlocking()
}


fun logProgramDuration(type: String,  durationTotal: Long, durationCmd1: Long = 0, durationCmd2: Long = 0, durationCmd3: Long = 0) {

    val client = OkHttpClient()

    val json = """
        {
            "event": "duration",
            "value1": "$type",
            "value2": $durationTotal,
            "value3": $durationCmd1,
            "value4": $durationCmd2,
            "value5": $durationCmd3
        }
        """.trimIndent()

    val requestBody = json.toRequestBody("application/json".toMediaType())

    val request = Request.Builder()
        .url("https://script.google.com/macros/s/AKfycby2AsUDKdggrbiVYUcwb46XhKaan4qerut3MdcAEVtZe2IiI9AkQGOZH7sy-aL4Unt5/exec")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        println("Antwort vom Webhook: ${response.body?.string()}")
    }
}