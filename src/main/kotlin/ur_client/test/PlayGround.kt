package com.wolfscowl.ur_client.test

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun getflow(delayTime: Long) =  flow{
    var number = 0
    repeat(50) {
        number++
        emit(number)
        delay(delayTime)
    }
}

fun printException(title: String, e: Throwable) {
    println("-=$title=-\n  Exception: $e\n" + "  Message: ${e.message}\n" + "  Cause:  ${e.cause}")
}

fun main() {
    runBlocking {
        val scope = CoroutineScope(Dispatchers.IO + Job())
        val parentJob = scope.launch {
            try {
                val job = Job()
                val deferred = async(job) {
                    val childJob1 = launch {
                        try {
                            getflow(20).collect { number ->
                                println("Child Job 1: $number")
                                if (number == 10)
                                    throw RuntimeException()
                            }
                        } catch (e: Exception) {
                            printException("child Job 1",e)
                            throw e
                        }
                    }
                    println("ChildJob1 Parent " + childJob1.parent)
                    val childJob2 = launch {
                        try {
                            getflow(20).collect { number ->
                                println("Child Job 2: $number")
                            }
                        } catch (e: Exception) {
                            printException("child Job 2",e)
                        }
                    }
                    getflow(20).collect { number ->
                        println("Parent Job : $number")
                    }
                }
                deferred.await()
                println("Async Job: " + deferred.job)
                println("Async Parent: " + deferred.parent)
                println("Alle gelaunchten Coroutines im Scope sind fertig")
            } catch (e: Exception) {
                printException("Parent Job",e)
                //Wenn man an die übergeordneten Default CoroutinesExceptionHandler weitergeben will
                // throw e
            } finally {
                println("Finally")
            }
        }
        println("Parent Job: " + parentJob)
        println("Im in RUnblocking")
        delay(10500)
    }
}




//fun main() = runBlocking {
//    println("--- Test: CancellationException wird nicht weitergegeben ---")
//
//    val scope = CoroutineScope(Job())
//
//    val job3 = scope.launch {
//        try {
//            launch {
//                try {
//                    delay(100)
//                    println("Coroutine 1 läuft")
//                    throw CancellationException("Manuelle Stornierung!")
//                } catch (e: Exception) {
//                    println("Coroutine 1 gefangen: ${e.message}")
//                    throw e // Wenn diese Zeile einkommentiert wird, würde die Exception nach außen gehen!
//                }
//            }
//            delay(300)
//            launch {
//                delay(300)
//                println("Coroutine 2 läuft weiter")
//            }
//        } catch (e: Exception) {
//            println("Father gets cancelExceptopon")
//        }
//
//    }
//
//
//    delay(800)
//    println("Scope noch aktiv? ${scope.isActive}") // Sollte true sein
//}

//fun main() = runBlocking {
//    println("--- CoroutineScope mit normalem Job ---")
//    val scope1 = CoroutineScope(Job())
//
//    scope1.launch {
//        delay(100)
//        println("Coroutine 1 läuft")
//        throw RuntimeException("Fehler in Coroutine 1")
//    }
//
//    scope1.launch {
//        delay(200)
//        println("Coroutine 2 läuft")
//    }
//
//    delay(300)
//    println("Scope 1 abgeschlossen? ${scope1.coroutineContext[Job]?.isActive == false}")
//
//    println("\n--- CoroutineScope mit SupervisorJob ---")
//    val scope2 = CoroutineScope(SupervisorJob())
//
//    scope2.launch {
//        delay(100)
//        println("Coroutine 3 läuft")
//        throw RuntimeException("Fehler in Coroutine 3")
//    }
//
//    scope2.launch {
//        delay(200)
//        println("Coroutine 4 läuft")
//    }
//
//    delay(300)
//    println("Scope 2 abgeschlossen? ${scope2.coroutineContext[Job]?.isActive == false}")
//}

//fun main() {
//    val flow1 =  flow{
//        var number = 0
//        repeat(100) {
//            number++
//            emit(number)
//            delay(100)
//        }
//    }
//
//    val flow2 =  flow{
//        var number = 0
//        repeat(100) {
//            number++
//            emit(number)
//            delay(100)
//        }
//    }
//
//    val scope = CoroutineScope(Dispatchers.IO + Job())
//    var father: Job? = null
//    var child1: Job? = null
//    var child2: Job? = null
//
//    runBlocking {
//        try {
//            coroutineScope{
//                father = scope.launch {
//                    child1 = launch {
//                        flow1.collect { value ->
//                            println("Number: " + value)
//                        }
//                    }
//                    child2 =launch {
//                        for(i in 0..100) {
//                            println("Hello $i")
//                            delay(100)
//                        }
//                    }
//                    println("Fertig")
//                }
//            }
//        } catch(e: CancellationException) {
//            throw e
//        }
//        delay(1000)
//        child2?.cancel()
//        println("Child cancelt")
//        delay(2000)
//    }
//
//}

// =================================================================================================
// =================================================================================================




//fun main() {
//    val flow1 =  flow{
//        var number = 0
//        repeat(100) {
//            number++
//            emit(number)
//            delay(100)
//        }
//    }
//
//    val flow2 =  flow{
//        var number = 0
//        repeat(100) {
//            number++
//            emit(number)
//            delay(100)
//        }
//    }
//
//
//    runBlocking {
//        try {
//            coroutineScope {
//                launch {
//                    flow1.collect { value ->
//                        println("Number: " + value)
//                    }
//                }
//                launch() {
//                    println("Coroutine startet jetzt!")
//                    delay(1000)
//                    println("Coroutine ist fertig!")
//                }
//                println("Fertig")
//            }
//            println("Fertig2")
//        } catch(e: CancellationException) {
//            throw e
//        }
//
//    }
//
//}

// =================================================================================================
// =================================================================================================

//data class Person(val name: String)
//
//fun main() {
//    var person1 = Person("Fritz")
//
//    // Hier wird ein Lambda übergeben, das den Updater empfängt und person1 aktualisiert.
//    handelSomething { updater ->
//        person1 = updater(person1)
//    }
//
//    println(person1) // Ausgabe: Person(name=Max)
//}
//
//// handelSomething nimmt als Parameter ein Lambda, das eine Funktion (Updater) vom Typ (Person) -> Person erhält.
//fun handelSomething(update: ((Person) -> Person) -> Unit) {
//    // Hier wird der Updater definiert, der person1 transformiert.
//    update { it.copy(name = "Max") }
//}


// =================================================================================================
// =================================================================================================


fun main4() {
    val flow =  flow{
        var number = 0
        repeat(100) {
            number++
            emit(number)
            delay(100)
        }
    }

    runBlocking {
        val job2 = launch(Dispatchers.IO,start = CoroutineStart.LAZY) {
            println("Coroutine startet jetzt!")
            delay(1000)
            println("Coroutine ist fertig!")
        }

        val job = launch {
            try {
                flow.collect { value ->
                    println("Number: " + value)
                }
            } catch (e: CancellationException) {
                println("Collect wurde abgebrochen")
            }
        }
        delay(1000)
        job.cancelAndJoin()
        println("Bin fertig")
    }


}

// =================================================================================================
// =================================================================================================



//fun main7() {
//    runBlocking {
//
//        launch {
//        launch {
//
//                withTimeout(100) { // Timeout nach 100 ms
//                    println("Start")
//                    delay(200) // Dauert zu lange
//                    println("Wird nicht ausgeführt!") // Wird nie erreicht
//                }
//
//            println("Hello1")
//        }
//            delay(300)
//            println("Hello2")
//        }
//    }
//}
//
//var acquired = 0
//
//class Resource {
//    init { acquired++ } // Acquire the resource
//    fun close() { acquired-- } // Release the resource
//}

// =================================================================================================
// =================================================================================================

//fun main8() {
//    runBlocking {
//        repeat(10_000) { // Launch 10K coroutines
//            launch {
//                val resource = withTimeout(60) { // Timeout of 60 ms
//                    delay(90) // Delay for 50 ms
//                    //println("Ressource wird erworben")
//                    Resource() // Acquire a resource and return it from withTimeout block
//                }
//                println("Ressource wird geschlossen")
//                resource.close() // Release the resource
//            }
//        }
//    }
//    // Outside of runBlocking all coroutines have completed
//    println(acquired) // Print the number of resources still acquired
//}

// =================================================================================================
// =================================================================================================

//fun main3() {
//    runBlocking {
//
//        val job = launch {
//            try {
//                repeat(1000) { i ->
//                    println("job: I'm sleeping $i ...")
//                    delay(500L)
//                }
//            } catch (e: CancellationException) {
//                println(e)
//            } finally {
//                withContext(NonCancellable) {
//                    withContext(Dispatchers.Default) {
//                        println("job: I'm running finally")
//                    }
//                }
//            }
//        }
//        delay(1300L) // delay a bit
//        println("main: I'm tired of waiting!")
//        job.cancelAndJoin() // cancels the job and waits for its completion
//        println("main: Now I can quit.")
//    }
//}


// =================================================================================================
// =================================================================================================

//fun main66() {
//    runBlocking {
//
//        val job = launch(Dispatchers.Default) {
//            try {
//            repeat(5) { i ->
//
//                    // print a message twice a second
//                    println("job: I'm sleeping $i ...")
//                    delay(500)
//
//            }
//            } catch (e: Exception) {
//                // log the exception
//                println(e)
//            }
//        }
//        delay(1300L) // delay a bit
//        println("main: I'm tired of waiting!")
//        job.cancelAndJoin() // cancels the job and waits for its completion
//        println("main: Now I can quit.")
//    }
//}