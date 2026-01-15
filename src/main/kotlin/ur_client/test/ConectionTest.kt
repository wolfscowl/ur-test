package com.wolfscowl.ur_client.test

import com.wolfscowl.ur_client.UR
import com.wolfscowl.ur_client.examples.Examples.ur
import com.wolfscowl.ur_client.model.element.WatchdogConfig
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    //val urHost = "192.168.178.32"
    //val urHost = "192.168.12.145"
    val urHost = "192.168.56.101"
    //val urHost = "192.168.1.15"
    //val urHost = "192.168.56.101"
    val onRobotHost = "192.168.12.245"

    runBlocking {
        val ur = UR(host = urHost, logRobotMessages = false, logRobotStates = false, watchdogConfig = WatchdogConfig())
        ur.connect()



    }
}