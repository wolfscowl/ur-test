package com.wolfscowl.ur_client.interfaces.state

import com.wolfscowl.ur_client.core.internal.state.MutableRGToolState

interface RGToolState: ToolState {
    val width: Float
    val depth: Float
    val gripDetected: Boolean

    override fun copy() : RGToolState

}