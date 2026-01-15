package com.wolfscowl.ur_client.core.internal.config

internal object Default {
    // URApi
    const val INTERFACE_PORT: Int = 30001
    const val DASHBOARD_PORT: Int = 29999
    const val CONNECT_TIMEOUT: Int = 1000
    const val SO_TIMEOUT: Int = 5000
    const val LOG_ROBOT_STATES: Boolean = false
    const val LOG_ROBOT_MESSAGES: Boolean = false


    // Watchdog
    const val SILENCE_TIMEOUT_MILLIS: Long = 1000
    const val MAX_RECONNECT_ATTEMPTS: Int = 3
    const val RECONNECT_WINDOW_SECONDS: Long = 10
    const val ENABLE_LOGGING = true
    const val LOG_PACKAGE_THRESHOLD_MILLIS: Long = 120


    // Arm
    const val BLEND_RADIUS: Double = 0.0
    const val ACCELERATION: Double = 1.0
    const val VELOCITY: Double = 0.8
    const val TIME: Double = 0.0



    // Tool
    const val CMD_TIMEOUT: Long = 10000
    const val GRIP_TIMEOUT = 3.0f
    const val POPUP_MSG: Boolean = true
}