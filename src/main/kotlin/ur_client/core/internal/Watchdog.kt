package com.wolfscowl.ur_client.core.internal

import com.wolfscowl.ur_client.core.internal.util.Util.log
import com.wolfscowl.ur_client.model.element.WatchdogConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import sun.util.logging.resources.logging

/**
 * Monitors network activity and triggers a reconnect if no data is received within the configured timeout.
 */
internal class Watchdog(
    private val scope: CoroutineScope,
    private val config: WatchdogConfig,
    private val connect: () -> Unit,
    private val disconnect: () -> Unit
) {
    private var lastHeartbeat = System.currentTimeMillis()
    private var job: Job? = null
    private val reconnectTimestamps = mutableListOf<Long>()

    fun start() {
        job = scope.launch {
            lastHeartbeat = System.currentTimeMillis()
            while (isActive) {
                delay(50L)
                val silence = System.currentTimeMillis() - lastHeartbeat
                if (silence > config.silenceTimeoutMillis) {
                    handleReconnect()
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
    }

    fun heartbeat() {
        val now = System.currentTimeMillis()
        val packageLatency = now - lastHeartbeat
        if (config.enableLogging && packageLatency > config.logPackageThresholdMillis )
            log("ℹ️ Watchdog: Package latency > ${config.logPackageThresholdMillis}ms: $packageLatency ms")
        lastHeartbeat = System.currentTimeMillis()
        lastHeartbeat = now
    }


    private fun handleReconnect() {
        val now = System.currentTimeMillis()

        // Remove old reconnect timestamps outside the time window
        if (config.reconnectWindowSeconds > 0) {
            val windowMillis = config.reconnectWindowSeconds * 1000
            reconnectTimestamps.removeAll { it < now - windowMillis }
        }

        // Count and enforce reconnect limit
        if (config.maxReconnectAttempts > 0 && reconnectTimestamps.size >= config.maxReconnectAttempts) {
            if (config.enableLogging) {
                log("⚠️ Watchdog: Maximum reconnect attempts (${config.maxReconnectAttempts}) reached within ${config.reconnectWindowSeconds}s → disconnecting")
            }
            disconnect()
            stop()
            return
        }

        // Perform reconnect
        reconnectTimestamps.add(now)
        if (config.enableLogging) {
            log("⚠️ Watchdog: No data for ${System.currentTimeMillis() - lastHeartbeat} ms → reconnecting (attempt ${reconnectTimestamps.size})")
        }
        disconnect()
        connect()
        lastHeartbeat = now
    }




    private fun log(message: String) {
        println(message)
    }
}
