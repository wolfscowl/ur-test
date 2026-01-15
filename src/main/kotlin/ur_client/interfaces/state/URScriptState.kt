package com.wolfscowl.ur_client.interfaces.state

import com.wolfscowl.ur_client.core.internal.state.MutableURScriptState
import com.wolfscowl.ur_client.model.element.Error
import com.wolfscowl.ur_client.model.element.RunningState
import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis

// ====================== Interfaces =====================

interface URScriptState {
    val scriptName: String
    val runningState: RunningState
    val safetyModeStop: Boolean
    val errors: List<Error>
    val errorOccurred: Boolean

    fun copy(): URScriptState
}


/**
 * Awaits for the final URScriptState without blocking the thread and returns it.
 * The state is final when `runningState` assumes the following value:
 * [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END]
 * [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT]
 * [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
 * @return Final URScriptState of the UR command
 *
 * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.coroutineAwait
 */
suspend inline fun <reified T : URScriptState> T.await(): T {
    while (this.runningState !in setOf(RunningState.END, RunningState.CANCELED, RunningState.PAUSED, RunningState.TIMEOUT))
        delay(10)
    return this
}


/**
 * Awaits for a specified condition on the URScriptState without blocking the thread and returns the state.
 * The waiting continues until the provided predicate returns true or the state becomes final.
 * The state is final when `runningState` assumes the following value:
 * [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END]
 * [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT]
 * [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
 *
 * @param predicate A condition that is evaluated on the URScriptState. The function waits until this condition is met.
 * @return The URScriptState when the condition is satisfied or a final state is reached.
 *
 * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.coroutineAwaitUntil
 */
suspend inline fun <reified T : URScriptState> T.awaitUntil(predicate: (T) -> Boolean): T {
    while (!predicate(this) && this.runningState !in setOf(RunningState.END, RunningState.CANCELED, RunningState.PAUSED, RunningState.TIMEOUT)) {
        delay(10)
    }
    return this
}


/**
 * Awaits for the final URScriptState with blocking the thread and returns it.
 * The state is final when `runningState` assumes the following value:
 * [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END]
 * [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT]
 * [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
 * @return Final URScriptState of the UR command
 *
 * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.threadAwait
 */
inline fun <reified T : URScriptState> T.awaitBlocking(): T {
    while (this.runningState !in setOf(RunningState.END, RunningState.CANCELED, RunningState.PAUSED, RunningState.TIMEOUT))
        Thread.sleep(20)
    return this
}


/**
 * Awaits for a specified condition on the URScriptState while blocking the thread and returns the state.
 * The waiting continues until the provided predicate returns true or the state becomes final.
 * The state is final when `runningState` assumes the following value:
 * [RunningState.END][com.wolfscowl.ur_client.model.element.RunningState.END]
 * [RunningState.TIMEOUT][com.wolfscowl.ur_client.model.element.RunningState.TIMEOUT]
 * [RunningState.CANCELED][com.wolfscowl.ur_client.model.element.RunningState.CANCELED]
 *
 * @param predicate A condition that is evaluated on the URScriptState. The function waits until this condition is met.
 * @return The URScriptState when the condition is satisfied or a final state is reached.
 *
 * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.threadAwaitUntil
 */
inline fun <reified T : URScriptState> T.awaitBlockingUntil(predicate: (T) -> Boolean): T {
    while (!predicate(this) && this.runningState !in setOf(RunningState.END, RunningState.CANCELED, RunningState.PAUSED, RunningState.TIMEOUT))
        Thread.sleep(20)
    return this
}





