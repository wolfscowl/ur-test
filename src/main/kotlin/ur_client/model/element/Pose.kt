package com.wolfscowl.ur_client.model.element

import com.wolfscowl.ur_client.core.internal.util.Util.round
import com.wolfscowl.ur_client.core.internal.util.Util.toStringFormat
import com.wolfscowl.ur_client.model.element.Vec3
import java.math.BigDecimal

data class Pose(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val rx: Double = 0.0,
    val ry: Double = 0.0,
    val rz: Double = 0.0,
) {

    fun isEqual(tcpPose: Pose, decimals: Int = 6): Boolean {
        return this.x.round(decimals) == tcpPose.x.round(decimals) &&
                this.y.round(decimals) == tcpPose.y.round(decimals) &&
                this.z.round(decimals) == tcpPose.z.round(decimals) &&
                this.rx.round(decimals) == tcpPose.rx.round(decimals) &&
                this.ry.round(decimals) == tcpPose.ry.round(decimals) &&
                this.rz.round(decimals) == tcpPose.rz.round(decimals)
    }


    override fun toString(): String {
        return buildString {
            append("-=Pose=-\n")
            append("  x = $x m\n")
            append("  y = $y m\n")
            append("  z = $z m\n")
            append("  rx = $rx rad\n")
            append("  ry = $ry rad\n")
            append("  rz = $rz rad")
        }
    }


    fun toFormattedString(
        rotInDegree: Boolean = false,
        posInMillimeter: Boolean = false,
        decimalsPos: Int = -1,
        decimalsRot: Int = -1
    ): String {
        val posUnit = if (posInMillimeter) "mm" else "m"
        val rotUnit = if (rotInDegree) "°" else "rad"

        val factor = if (posInMillimeter) 1000.0 else 1.0

        val rxVal = if (rotInDegree) Math.toDegrees(rx) else rx
        val ryVal = if (rotInDegree) Math.toDegrees(ry) else ry
        val rzVal = if (rotInDegree) Math.toDegrees(rz) else rz

        fun format(value: Double, decimals: Int): String {
            return if (decimals >= 0) {
                String.format("%.${decimals}f", value).replace(',', '.')
            } else {
                value.toString()
            }
        }

        return buildString {
            append("-=Pose=-\n")
            append("  x = ${format(x * factor, decimalsPos)} $posUnit\n")
            append("  y = ${format(y * factor, decimalsPos)} $posUnit\n")
            append("  z = ${format(z * factor, decimalsPos)} $posUnit\n")
            append("  rx = ${format(rxVal, decimalsRot)} $rotUnit\n")
            append("  ry = ${format(ryVal, decimalsRot)} $rotUnit\n")
            append("  rz = ${format(rzVal, decimalsRot)} $rotUnit")
        }
    }


    internal fun toCmdString(): String {
        return buildString {
            append("p[")
            append(x)
            append(",")
            append(y)
            append(",")
            append(z)
            append(",")
            append(rx)
            append(",")
            append(ry)
            append(",")
            append(rz)
            append("]")
        }
    }


}