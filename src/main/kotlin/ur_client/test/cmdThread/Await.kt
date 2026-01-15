package com.wolfscowl.ur_client.test.cmdThread

import com.wolfscowl.ur_client.model.element.RunningState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking



// =================================================================================================
// ====================================== Override Methode =========================================
// =================================================================================================

open class BaseState {
    var scriptName: String = ""
    var runningState: RunningState = RunningState.PENDING
    var safetyModeStop: Boolean = false

    // Basismethode, die den BaseState zurückgibt
    open suspend fun await(): BaseState {
        while (runningState !in setOf(
                RunningState.END,
                RunningState.CANCELED,
                RunningState.PAUSED,
                RunningState.TIMEOUT
            )
        ) {
            delay(100) // Gibt den Thread für andere Coroutines frei
        }
        return this
    }
}

class ToolState : BaseState() {
    var toolName: String = ""

    // Überschreibt die Methode und liefert ToolState zurück
    override suspend fun await(): ToolState {
        return super.await() as ToolState
    }
}

class ArmState : BaseState() {
    var armName: String = ""

    // Überschreibt die Methode und liefert ArmState zurück
    override suspend fun await(): ArmState {
        return super.await() as ArmState
    }
}

fun main() = runBlocking {
    val toolState = ToolState().apply { toolName = "MyTool" }
    launch {
        delay(3000) // Simuliert den Zustandswechsel
        toolState.runningState = RunningState.END
    }

    // Hier wird finalState automatisch als ToolState erkannt.
    val finalState: ToolState = toolState.await()
    println("Finaler State: ToolState mit Name: ${finalState.toolName}")
}



// =================================================================================================
// ==================================BaseSTate Generic==============================================
// =================================================================================================

//open class BaseState<T : BaseState<T>> {
//    var scriptName: String = ""
//    var runningState: RunningState = RunningState.PENDING
//    var safetyModeStop: Boolean = false
//
//    @Suppress("UNCHECKED_CAST")
//    suspend fun await(): T {
//        while (runningState !in setOf(
//                RunningState.END,
//                RunningState.CANCELED,
//                RunningState.PAUSED,
//                RunningState.TIMEOUT
//            )
//        ) {
//            delay(100)
//        }
//        return this as T
//    }
//}
//
//// Der konkrete Typ muss nun die Basisklasse mit sich selbst als Typparameter erweitern.
//class ToolState : BaseState<ToolState>() {
//    var toolName: String = ""
//}
//
//class ArmState : BaseState<ArmState>() {
//    var armName: String = ""
//}
//
//fun main() = runBlocking {
//    val baseState =
//    val toolState = ToolState().apply { toolName = "MyTool" }
//    launch {
//        delay(3000) // Simuliert den Zustandswechsel
//        toolState.runningState = RunningState.END
//    }
//
//    // Hier ist finalState vom Typ ToolState, ohne extra Prüfungen oder Casts.
//    val finalState = toolState.await()
//    println("Finaler State: ToolState mit Name: ${finalState.toolName}")
//}

// =================================================================================================
// ==================================Generic Extension Function=====================================
// =================================================================================================
//
//open class BaseState {
//    var scriptName: String = ""
//    var runningState: RunningState = RunningState.PENDING
//    var safetyModeStop: Boolean = false
//
//    fun awaitEndStateBlocking() {
//        while (runningState != RunningState.END) {
//            Thread.sleep(100) // Blockiert den Thread
//        }
//    }
//
//}
//
//suspend inline fun <reified T : BaseState> T.await(): T {
//    while (this.runningState !in setOf(
//            RunningState.END,
//            RunningState.CANCELED,
//            RunningState.PAUSED,
//            RunningState.TIMEOUT
//        )
//    ) {
//        delay(100) // Gibt den Thread für andere Coroutines frei
//    }
//    return this
//}
//
//class ToolState: BaseState() {
//    var toolName: String = ""
//}
//
//class ArmState: BaseState() {
//    var armName: String = ""
//}
//
//
//fun main() =  runBlocking {
//    val toolState = ToolState()
//    launch() {
//        delay(9000)
//        toolState.runningState = RunningState.END
//    }
//    val state = toolState.await()
//    println("RunningState END")
//}