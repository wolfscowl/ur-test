package com.wolfscowl.ur_client.interfaces.state


interface VGToolSeekState: VGToolState {
    val vacuumReached: Boolean
    val objectDetection: Boolean

    override fun copy() : VGToolSeekState
}
