package com.wolfscowl.ur_client.interfaces.state

import com.wolfscowl.ur_client.core.internal.state.MutableTFGToolState

interface TFGToolState: ToolState {
    val extWidth: Float
    val intWidth: Float
    val gripDetected: Boolean

    override fun copy() : TFGToolState
}