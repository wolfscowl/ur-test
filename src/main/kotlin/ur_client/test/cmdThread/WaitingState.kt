package com.wolfscowl.ur_client.test.cmdThread

import com.wolfscowl.ur_client.model.element.RunningState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ScriptState(
    @Volatile var scriptName: String = ""
)  {
    @Volatile
    var runningState: RunningState = RunningState.PENDING
    @Volatile
    var safetyModeStop: Boolean = false

    override fun toString(): String {
        return buildString {
            append("-=URScriptState=-\n")
            append("  runningState = $runningState\n")
            append("  safetyModeStop = $safetyModeStop\n")
        }
    }



    // Coroutine-freundliche Wartefunktion
//    suspend fun awaitEndState2() = suspendCancellableCoroutine<Unit> { continuation ->
//        thread {
//            while (runningState != RunningState.END) {
//                Thread.sleep(100) // Verhindert hohe CPU-Last
//            }
//            continuation.resume(Unit) {}
//        }
//    }

    fun awaitEndStateBlocking() {
        while (runningState != RunningState.END) {
            Thread.sleep(100) // Blockiert den Thread
        }
    }

    suspend fun await() {
        while (runningState in setOf(
                RunningState.END,
                RunningState.CANCELED,
                RunningState.PAUSED,
                RunningState.TIMEOUT)) {
            delay(100) // Gibt den Thread für andere Coroutines frei
        }
    }

}


fun main() = runBlocking {
    val scriptState = ScriptState()

    // Startet eine Coroutine, die auf den Endzustand wartet
    launch {
        println("Warte auf END-Status...")
        scriptState.await()
        if (scriptState.safetyModeStop)
            "dfff"
        else
            "dffddf"
        println("Status ist jetzt END!")
    }

    Thread {
        println("Thread wartet auf END...")
        scriptState.awaitEndStateBlocking() // Läuft hier blockierend mit `Thread.sleep(100)`
        println("Thread: Status ist jetzt END!")
    }.start()

    // Simuliert eine Verzögerung und ändert dann den Status
    delay(2000)
    scriptState.runningState = RunningState.END
    println("runningState wurde auf END gesetzt.")
}