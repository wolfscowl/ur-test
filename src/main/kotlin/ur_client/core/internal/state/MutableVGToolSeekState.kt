package com.wolfscowl.ur_client.core.internal.state

import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.interfaces.state.VGToolSeekState

internal class MutableVGToolSeekState(
    scriptName: String = ""
): MutableVGToolState(scriptName), VGToolSeekState {
    @Volatile
    override var vacuumReached: Boolean = false
    @Volatile
    override var objectDetection: Boolean = false

    override fun toString(): String {
        return buildString {
            append("-=VGToolSeekState=-\n")
            append(super.toString().formatString() + "\n")
            append("  vacuumReached = $vacuumReached\n")
            append("  objectDetection = $objectDetection")
        }
    }

    override fun copy() : MutableVGToolSeekState =
        copyBaseTo(MutableVGToolSeekState()).also {
            it.vacuumReached = this.vacuumReached
            it.objectDetection = this.objectDetection
        }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        if (other !is MutableVGToolSeekState) return false
        return  vacuumReached == other.vacuumReached &&
                objectDetection == other.objectDetection
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + vacuumReached.hashCode() + objectDetection.hashCode()
    }
}