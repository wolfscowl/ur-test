package com.wolfscowl.ur_client.model.element

data class Inertia(
    val xx: Float = 0.000000f,
    val yy: Float = 0.000000f,
    val zz: Float = 0.000000f,
    val xy: Float = 0.000000f,
    val xz: Float = 0.000000f,
    val yz: Float = 0.000000f
) {
    override fun toString(): String {
        return buildString {
            append("-=InertiaMatrix=-\n")
            append("  xx = $xx\n")
            append("  yy = $yy\n")
            append("  zz = $zz\n")
            append("  xy = $xy\n")
            append("  xz = $xz\n")
            append("  yz = $yz")
        }
    }

    internal fun toCmdString(): String = "[$xx,$yy,$zz,$xy,$xz,$yz]"
}
