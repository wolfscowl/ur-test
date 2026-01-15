package com.wolfscowl.ur_client.test

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

fun main() {
    runBlocking {
        GlobalScope.launch {
            try {
                doSomething()
                delay(1500)
                println()
            } catch(e: Exception) {
                println("From main: $e" )
            }

        }
        delay(10000)
    }
}


suspend fun doSomething() {
    try {
        withTimeout(1000) {
            delay(2000)
        }
    } catch(e: Exception) {
        println("From Suspendfun: $e" )
    }
}