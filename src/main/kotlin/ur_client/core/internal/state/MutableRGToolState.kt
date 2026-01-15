package com.wolfscowl.ur_client.core.internal.state

import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.interfaces.state.RGToolState

internal class MutableRGToolState(
    scriptName: String = ""
) : MutableToolState(scriptName), RGToolState {
    @Volatile
    override var width: Float = -1000.0f
    @Volatile
    override var depth: Float = -1000.0f
    @Volatile
    override var gripDetected: Boolean = false

    override fun toString(): String {
        return buildString {
            append("-=RGToolState=-\n")
            append(super.toString().formatString() + "\n")
            append("  width = $width\n")
            append("  depth = $depth\n")
            append("  gripDetected = $gripDetected")
        }
    }

    override fun copy() : MutableRGToolState =
        copyBaseTo(MutableRGToolState()).also {
            it.toolDetected = this.toolDetected
            it.width = this.width
            it.depth = this.depth
            it.gripDetected = this.gripDetected
        }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        if (other !is MutableRGToolState) return false
        return  width == other.width &&
                depth == other.depth &&
                gripDetected == other.gripDetected
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() +
                width.hashCode() +
                depth.hashCode() +
                gripDetected.hashCode()
    }
}