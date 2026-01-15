package com.wolfscowl.ur_client.model.element

import com.wolfscowl.ur_client.core.internal.config.Default


/**
 * Configuration settings for the internal connection watchdog mechanism.
 *
 * The watchdog monitors communication with the robot and can trigger reconnects,
 * track latency, and optionally log its activity.
 *
 * @param silenceTimeoutMillis Time span of inactivity after which a reconnect
 * is triggered, in milliseconds.
 * **Default: 1000**
 *
 * @param maxReconnectAttempts Maximum number of reconnect attempts
 * (≤ 0 means unlimited).
 * **Default: 3**
 *
 * @param reconnectWindowSeconds Time window in which reconnect attempts are counted,
 * in seconds (≤ 0 means unlimited).
 * **Default: 10**
 *
 * @param enableLogging Enables or disables watchdog activity logging.
 * **Default: true**
 *
 * @param logPackageThresholdMillis Time after which package latency is logged,
 * in milliseconds (only if logging is enabled).
 * **Default: 120**
 */
data class WatchdogConfig(
    val silenceTimeoutMillis: Long = Default.SILENCE_TIMEOUT_MILLIS,
    val maxReconnectAttempts: Int = Default.MAX_RECONNECT_ATTEMPTS,
    val reconnectWindowSeconds: Long = Default.RECONNECT_WINDOW_SECONDS,
    val enableLogging: Boolean = Default.ENABLE_LOGGING,
    val logPackageThresholdMillis: Long = Default.LOG_PACKAGE_THRESHOLD_MILLIS
)