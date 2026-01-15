package com.wolfscowl.ur_client.interfaces.state

import com.wolfscowl.ur_client.core.internal.state.MutableVGToolReleaseState

interface VGToolReleaseState: VGToolState {
    val vacuumReleased: Boolean

    override fun copy() : VGToolReleaseState
}