package com.wolfscowl.ur_client.core.internal.state


import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.interfaces.state.ToolState


internal open class MutableToolState(
    scriptName: String = "",
): MutableURScriptState(scriptName), ToolState {
    @Volatile
    override var toolDetected: Boolean = false

    override fun toString(): String {
        return buildString {
            append("-=ToolState=-\n")
            append(super.toString().formatString() + "\n")
            append("  toolDetected = $toolDetected")
        }
    }

    override fun copy() : MutableToolState = copyBaseTo(MutableToolState())

    protected fun <T: MutableToolState>copyBaseTo(target: T): T {
        val target = super.copyBaseTo(target)
        target.toolDetected = this.toolDetected
        return target
    }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        if (other !is MutableToolState) return false
        return toolDetected == other.toolDetected
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() +
                toolDetected.hashCode()
    }

}