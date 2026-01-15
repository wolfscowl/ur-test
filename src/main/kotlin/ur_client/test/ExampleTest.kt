package com.wolfscowl.ur_client.test

import com.wolfscowl.ur_client.UR
import com.wolfscowl.ur_client.interfaces.state.await
import com.wolfscowl.ur_client.model.element.RunningState
import kotlinx.coroutines.runBlocking

fun main() {
    val ur = UR(host="192.168.56.101")
    ur.connect()

    runBlocking {
        val state = ur.runURScript(
            script = """
                movej([0,4.71,-1.57,3.14,-1.57,-1.57], a=1.4, v=1.05, t=0, r=0)
            """.trimIndent(),
            cmdTimeout = 5000,
            onChange = { },
            onFinished = { state ->
                if (state.runningState == RunningState.END && !state.errorOccurred)
                    println("The script was executed successfully.")
            }
        )
        println(state.runningState) // print "PENDING"
        //state.await()               // will wait or the finale state
        println(state.runningState) // print "TIMEOUT" or "END"
    }
}


fun main267() {
    println("270 -> " + Math.toRadians(270.0))
    println("-90 -> " + Math.toRadians(-90.0))
    println("180 -> " + Math.toRadians(180.0))
}