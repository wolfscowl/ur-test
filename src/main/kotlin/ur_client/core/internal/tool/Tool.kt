package com.wolfscowl.ur_client.core.internal.tool

import com.wolfscowl.ur_client.core.internal.cmd_handler.CmdHandler
import com.wolfscowl.ur_client.core.internal.state.MutableToolState
import com.wolfscowl.ur_client.model.element.RunningState
import com.wolfscowl.ur_client.core.internal.event.URScriptEvent
import com.wolfscowl.ur_client.core.internal.state.MutableArmState
import com.wolfscowl.ur_client.core.internal.state.MutableURScriptState
import com.wolfscowl.ur_client.interfaces.state.ArmState
import com.wolfscowl.ur_client.interfaces.state.URScriptState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.cancellation.CancellationException

internal abstract class Tool {
     protected abstract val ur: URToolController
     protected abstract val urScriptEventFlow: SharedFlow<URScriptEvent>
     abstract val model: String
     private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
     protected abstract fun createScript(scriptName: String, scriptTask: String, popupMsg: Boolean): String

     protected fun <T : MutableToolState> execute(
         scriptName: String,
         script: String,
         cmdTimeout: Long? = null,
         state: T,
         collectEvent: suspend (()->Boolean,String,suspend ((T) -> Unit) -> Unit) -> Unit,
         onChange: ((T) -> Unit)? = null,
         onFinished: ((T) -> Unit)? = null
     ): T {
         val stateLock = Mutex()
         val collectStarted = CompletableDeferred<Unit>()
         val onCollectStarted = { collectStarted.complete(Unit) }
         CmdHandler.job?.cancel()

         CmdHandler.job = scope.launch {
             try {
                 if (!ur.checkExecutionConditions(state)) {
                     launch(NonCancellable) { onChange?.invoke(state as T) }
                     cancel()
                 }
                 launch {
                     collectStarted.await()
                     ur.sendURScript(script)
                 }
                 withCmdTimeout(cmdTimeout) {
                     collectEvent(onCollectStarted, scriptName) { updateState ->
                         ur.updateAndNotify(this, state, stateLock, onChange) { currentState ->
                             updateState(currentState)
                         }
                     }
                 }
             } catch (e: TimeoutCancellationException) {
                 state.runningState = RunningState.TIMEOUT
             } catch (e: CancellationException) {
                 state.runningState = RunningState.CANCELED
             } finally {
                 withContext(NonCancellable) {
                     onFinished?.invoke(state as T)
                 }
             }
         }
         return state
     }


    suspend inline fun <T> CoroutineScope.withCmdTimeout(
        timeout: Long?,
        crossinline execution: suspend CoroutineScope.() -> T
    ): T {
        return if (timeout != null) {
            withTimeout(timeout) {
                execution()
            }
        } else {
            execution()
        }
    }



}
