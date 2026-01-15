package com.wolfscowl.ur_client.core.internal

import com.wolfscowl.ur_client.model.element.WatchdogConfig

internal data class URConfig(
    val host: String,
    val interfacePort: Int,
    val dashBoardPort: Int,
    val connectTimeout: Int,
    val soTimeout: Int,
    val watchdogConfig: WatchdogConfig,
    val logRobotStates: Boolean,
    val logRobotMessages: Boolean
)
