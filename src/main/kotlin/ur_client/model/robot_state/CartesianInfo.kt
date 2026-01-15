package com.wolfscowl.ur_client.model.robot_state

// ROBOT_STATE_PACKAGE_TYPE_CARTESIAN_INFO = 4
data class CartesianInfo (
    val x: Double,              // double
    val y: Double,              // double
    val z: Double,              // double
    val rX: Double,             // double
    val rY: Double,             // double
    val rZ: Double,             // double
    val tcpOffsetX: Double,     // double
    val tcpOffsetY: Double,     // double
    val tcpOffsetZ: Double,     // double
    val tcpOffsetRx: Double,    // double
    val tcpOffsetRy: Double,    // double
    val tcpOffsetRz: Double     // double
): RobotState  {
    override fun toString(): String {
        return buildString {
            append("-=CartesianInfo=-\n")
            append("  x = $x\n")
            append("  y = $y\n")
            append("  z = $z\n")
            append("  rx = $rX\n")
            append("  ry = $rY\n")
            append("  rz = $rZ\n")
            append("  tcpOffsetX = $tcpOffsetX\n")
            append("  tcpOffsetY = $tcpOffsetY\n")
            append("  tcpOffsetZ = $tcpOffsetZ\n")
            append("  tcpOffsetRx = $tcpOffsetRx\n")
            append("  tcpOffsetRy = $tcpOffsetRy\n")
            append("  tcpOffsetRz = $tcpOffsetRz")
        }
    }
}