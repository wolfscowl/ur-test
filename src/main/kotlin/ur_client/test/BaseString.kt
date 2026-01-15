package com.wolfscowl.ur_client.test

import com.wolfscowl.ur_client.core.internal.state.MutableArmState
import com.wolfscowl.ur_client.core.internal.state.MutableRGToolState
import com.wolfscowl.ur_client.core.internal.state.MutableToolState
import com.wolfscowl.ur_client.core.internal.state.MutableURScriptState
import com.wolfscowl.ur_client.core.internal.state.MutableVGToolGripState
import com.wolfscowl.ur_client.core.internal.state.MutableVGToolReleaseState
import com.wolfscowl.ur_client.core.internal.state.MutableVGToolSeekState
import com.wolfscowl.ur_client.core.internal.state.MutableVGToolState
import com.wolfscowl.ur_client.model.element.Inertia


fun main() {
    println(MutableVGToolGripState())
    println(MutableVGToolSeekState())
    println(Inertia())
}
