package com.wolfscowl.ur_client.test

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val scope = this

    val stateFlow = MutableStateFlow("A") // Hat immer einen Wert
    val sharedFlow = MutableSharedFlow<Int>(replay = 1) // Speichert den letzten Wert

    scope.launch {
        combine(stateFlow, sharedFlow) { letter, number ->
            "StateFlow: $letter, SharedFlow: $number"
        }.collect { println(it) }
    }

    delay(500)
    stateFlow.value = "B"
    // Erstes Event → combine wird jetzt aktiv
    delay(500)
    sharedFlow.emit(1)
    // Ändert sich → combine wird erneut getriggert
    delay(500)
    sharedFlow.emit(2) // Ändert sich → combine wird erneut getriggert

    delay(1000)
}