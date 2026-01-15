package com.wolfscowl.ur_client.model.element

import com.wolfscowl.ur_client.core.internal.util.Util.round
import java.math.BigDecimal

data class JointPosition(
    val base: Double = 0.0,
    val shoulder: Double  = 0.0,
    val elbow: Double  = 0.0,
    val wrist1: Double  = 0.0,
    val wrist2: Double  = 0.0,
    val wrist3: Double  = 0.0,
) {

    fun isEqual(jointPosition: JointPosition, decimals: Int = 5): Boolean {
        return this.base.round(decimals) == jointPosition.base.round(decimals) &&
                this.shoulder.round(decimals) == jointPosition.shoulder.round(decimals) &&
                this.elbow.round(decimals) == jointPosition.elbow.round(decimals) &&
                this.wrist1.round(decimals) == jointPosition.wrist1.round(decimals) &&
                this.wrist2.round(decimals) == jointPosition.wrist2.round(decimals) &&
                this.wrist3.round(decimals) == jointPosition.wrist3.round(decimals)
    }

    override fun toString(): String {
        return buildString {
            append("-=JointPosition=-\n")
            append("  base = $base rad\n")
            append("  shoulder = $shoulder rad\n")
            append("  elbow = $elbow rad\n")
            append("  wrist1 = $wrist1 rad\n")
            append("  wrist2 = $wrist2 rad\n")
            append("  wrist3 = $wrist3 rad")
        }
    }

    fun toFormattedString(inDegree: Boolean = false, decimals: Int = -1): String {
        fun format(value: Double): String {
            return if (decimals >= 0) {
                String.format("%.${decimals}f", value).replace(',', '.')
            } else {
                value.toString()
            }
        }
        return if (inDegree) {
            buildString {
                append("-=JointPosition=-\n")
                append("  base = ${format(Math.toDegrees(base))} °\n")
                append("  shoulder = ${format(Math.toDegrees(shoulder))} °\n")
                append("  elbow = ${format(Math.toDegrees(elbow))} °\n")
                append("  wrist1 = ${format(Math.toDegrees(wrist1))} °\n")
                append("  wrist2 = ${format(Math.toDegrees(wrist2))} °\n")
                append("  wrist3 = ${format(Math.toDegrees(wrist3))} °")
            }
        } else {
            toString()
        }
    }

    internal fun toCmdString(): String {
        return buildString {
            append("[")
            append(base)
            append(",")
            append(shoulder)
            append(",")
            append(elbow)
            append(",")
            append(wrist1)
            append(",")
            append(wrist2)
            append(",")
            append(wrist3)
            append("]")
        }
    }


}