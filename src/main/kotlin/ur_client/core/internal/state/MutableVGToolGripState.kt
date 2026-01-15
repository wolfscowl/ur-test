package com.wolfscowl.ur_client.core.internal.state


import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.interfaces.state.VGToolGripState

internal class MutableVGToolGripState(
    scriptName: String = ""
): MutableVGToolState(scriptName), VGToolGripState {
    @Volatile
    override var vacuumReached: Boolean = false

    override fun toString(): String {
        return buildString {
            append("-=VGToolGripState=-\n")
            append(super.toString().formatString() + "\n")
            append("  vacuumReached = $vacuumReached")
        }
    }

    override fun copy() : MutableVGToolGripState =
        copyBaseTo(MutableVGToolGripState()).also {
            it.vacuumReached = this.vacuumReached
        }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        if (other !is MutableVGToolGripState) return false
        return vacuumReached == other.vacuumReached
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + vacuumReached.hashCode()
    }
}