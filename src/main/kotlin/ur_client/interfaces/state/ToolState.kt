package com.wolfscowl.ur_client.interfaces.state

import com.wolfscowl.ur_client.core.internal.state.MutableToolState

interface ToolState: URScriptState {
    val toolDetected: Boolean

    override fun copy() : ToolState
}
