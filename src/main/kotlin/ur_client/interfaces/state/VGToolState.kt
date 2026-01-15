package com.wolfscowl.ur_client.interfaces.state


interface VGToolState: ToolState {
    val vacuumA: Float?
    val vacuumB: Float?

    override fun copy() : VGToolState
}