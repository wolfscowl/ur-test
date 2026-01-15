package com.wolfscowl.ur_client.core.internal.tool.onrobot

import com.wolfscowl.ur_client.core.internal.tool.Tool

internal abstract class OnRobotTool: Tool() {
    abstract val host: String
    abstract val toolIndex: Int
}