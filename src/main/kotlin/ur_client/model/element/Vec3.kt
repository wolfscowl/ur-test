package com.wolfscowl.ur_client.model.element

data class Vec3(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
) {
    override fun toString(): String {
        return buildString {
            append("-=Vector3=-\n")
            append("  x = $x\n")
            append("  y = $y\n")
            append("  z = $z")
        }
    }

    internal fun toCmdString(): String = "[$x,$y,$z]"
}
