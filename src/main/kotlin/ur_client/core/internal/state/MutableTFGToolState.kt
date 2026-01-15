package com.wolfscowl.ur_client.core.internal.state

import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.interfaces.state.TFGToolState

internal class MutableTFGToolState(
    scriptName: String = ""
): MutableToolState(scriptName), TFGToolState {
    @Volatile
    override var extWidth: Float = -1000.0f
    @Volatile
    override var intWidth: Float = -1000.0f
    @Volatile
    override var gripDetected: Boolean = false

    override fun toString(): String {
        return buildString {
            append("-=TFGToolState=-\n")
            append(super.toString().formatString() + "\n")
            append("  extWidth = $extWidth\n")
            append("  intWidth = $intWidth\n")
            append("  gripDetected = $gripDetected")
        }
    }

    override fun copy() : MutableTFGToolState =
        super.copyBaseTo(MutableTFGToolState()).also {
            it.toolDetected = this.toolDetected
            it.extWidth = this.extWidth
            it.intWidth = this.intWidth
            it.gripDetected = this.gripDetected
        }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        if (other !is MutableTFGToolState) return false
        return  extWidth == other.extWidth &&
                intWidth == other.intWidth &&
                gripDetected == other.gripDetected
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() +
                extWidth.hashCode() +
                intWidth.hashCode() +
                gripDetected.hashCode()
    }
}