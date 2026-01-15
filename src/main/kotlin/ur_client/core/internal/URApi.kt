package com.wolfscowl.ur_client.core.internal

import com.wolfscowl.ur_client.core.URDashBoard
import com.wolfscowl.ur_client.core.URInterface
import com.wolfscowl.ur_client.UR

internal class URApi(config: URConfig):
    UR,
    URInterface by URInterfaceApi(
        host = config.host,
        interfacePort = config.interfacePort,
        connectTimeout = config.connectTimeout,
        watchdogConfig = config.watchdogConfig,
        logRobotStates = config.logRobotStates,
        logRobotMessages = config.logRobotMessages
    ),
    URDashBoard by URDashBoardApi(
        host = config.host,
        dashBoardPort = config.dashBoardPort,
        connectTimeout = config.connectTimeout,
        soTimeout = config.soTimeout,
    )
