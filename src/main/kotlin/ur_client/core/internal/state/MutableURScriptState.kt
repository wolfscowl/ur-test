package com.wolfscowl.ur_client.core.internal.state


import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.interfaces.state.URScriptState
import com.wolfscowl.ur_client.model.element.Error
import com.wolfscowl.ur_client.model.element.RunningState


internal open class MutableURScriptState(
    @Volatile override var scriptName: String = ""
): URScriptState {
    @Volatile
    override var runningState: RunningState = RunningState.PENDING
    @Volatile
    override var safetyModeStop: Boolean = false
    @Volatile
    override var errors: MutableList<Error> = mutableListOf()
    override val errorOccurred: Boolean
        get() = errors.isNotEmpty()


    override fun toString(): String {
        return buildString {
            append("-=URScriptState=-\n")
            append("  scriptName = $scriptName\n")
            append("  runningState = $runningState\n")
            append("  safetyModeStop = $safetyModeStop\n")
            append("  errorOccurred = $errorOccurred")
            if (errorOccurred) {
                append("\n  errors = \n")
                errors.forEachIndexed { index, error ->
                    val lines = error.toString().lines().toMutableList()
                    lines[1] = "${index+1}. ${lines[1]}"
                    append(lines.joinToString("\n").formatString(true,4))
                    if (index != errors.lastIndex)
                        append("\n")
                }
            }
        }
    }

    override fun copy(): MutableURScriptState = copyBaseTo(MutableURScriptState())

    protected open fun <T: MutableURScriptState>copyBaseTo(target: T): T {
        target.scriptName = this.scriptName
        target.runningState = this.runningState
        target.safetyModeStop = this.safetyModeStop
        target.errors = this.errors.toMutableList()
        return target
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MutableURScriptState) return false

        return  scriptName == other.scriptName &&
                runningState == other.runningState &&
                safetyModeStop == other.safetyModeStop &&
                errors == other.errors
    }

    override fun hashCode(): Int {
        var result = scriptName.hashCode()
        result = 31 * result + runningState.hashCode()
        result = 31 * result + safetyModeStop.hashCode()
        result = 31 * result + errors.hashCode()
        return result
    }

}