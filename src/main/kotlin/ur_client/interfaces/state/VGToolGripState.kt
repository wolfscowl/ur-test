package com.wolfscowl.ur_client.interfaces.state

import com.wolfscowl.ur_client.core.internal.state.MutableVGToolGripState

interface VGToolGripState: VGToolState {
    val vacuumReached: Boolean

    override fun copy() : VGToolGripState
}