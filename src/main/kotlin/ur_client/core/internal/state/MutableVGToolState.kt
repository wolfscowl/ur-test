package com.wolfscowl.ur_client.core.internal.state


import com.wolfscowl.ur_client.core.internal.util.Util.formatString
import com.wolfscowl.ur_client.interfaces.state.VGToolState

internal open class MutableVGToolState(
    scriptName: String = ""
): MutableToolState(scriptName), VGToolState {
    @Volatile
    override var vacuumA: Float = -1000.0f
    @Volatile
    override var vacuumB: Float = -1000.0f

    override fun toString(): String {
        return buildString {
            append("-=VGToolState=-\n")
            append(super.toString().formatString() + "\n")
            append("  vacuumA = $vacuumA\n")
            append("  vacuumB = $vacuumB")
        }
    }

    protected fun <T: MutableVGToolState>copyBaseTo(target: T): T {
        val target = super.copyBaseTo(target)
        target.vacuumA = this.vacuumA
        target.vacuumB = this.vacuumB
        return target
    }

    override fun copy() : MutableVGToolState =
        copyBaseTo(MutableVGToolState()).also {
            it.toolDetected = this.toolDetected
            it.vacuumA = this.vacuumB
        }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        if (other !is MutableVGToolState) return false
        return  vacuumA == other.vacuumA &&
                vacuumB == other.vacuumB
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + vacuumA.hashCode() + vacuumB.hashCode()
    }
}