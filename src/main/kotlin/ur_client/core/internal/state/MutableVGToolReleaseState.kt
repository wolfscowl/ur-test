package com.wolfscowl.ur_client.core.internal.state

import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.interfaces.state.VGToolReleaseState

internal class MutableVGToolReleaseState(
    scriptName: String = ""
): MutableVGToolState(scriptName), VGToolReleaseState {
    @Volatile
    override var vacuumReleased: Boolean = false

    override fun toString(): String {
        return buildString {
            append("-=VGToolReleaseState=-\n")
            append(super.toString().formatString() + "\n")
            append("  vacuumReleased = $vacuumReleased")
        }
    }

    override fun copy() : MutableVGToolReleaseState =
        copyBaseTo(MutableVGToolReleaseState()).also {
            it.vacuumReleased = this.vacuumReleased
        }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        if (other !is MutableVGToolReleaseState) return false
        return vacuumReleased == other.vacuumReleased
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + vacuumReleased.hashCode()
    }
}
