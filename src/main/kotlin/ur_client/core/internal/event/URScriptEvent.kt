package com.wolfscowl.ur_client.core.internal.event

import com.wolfscowl.ur_client.model.element.Error
import com.wolfscowl.ur_client.model.element.Inertia
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.RunningState
import com.wolfscowl.ur_client.model.element.Vec3
import com.wolfscowl.ur_client.model.robot_message.KeyMessage
import com.wolfscowl.ur_client.model.robot_message.RuntimeExceptionMessage
import com.wolfscowl.ur_client.model.robot_message.SafetyModeMessage
import com.wolfscowl.ur_client.model.robot_message.TextMessage
import com.wolfscowl.ur_client.model.robot_message.type.SafetyModeType


internal sealed class URScriptEvent {
    // General Events
    data class ExecutionState(val timestamp: ULong, val scriptName: String, val state: RunningState): URScriptEvent()
    data class SafetyModeStop(val timestamp: ULong, val state: SafetyModeType): URScriptEvent()
    data class ScriptError(val timestamp: ULong, val scriptName: String, val title: String, val message: String): URScriptEvent() {
        fun toError(): Error = Error(title = title, message = message)
    }
    data class RuntimeException(val timestamp: ULong, val scriptLineNumber: Int, val scriptColumnNumber: Int, val message: String): URScriptEvent() {
        fun toError(): Error =
            Error( title = "RuntimeException", message = buildString { append("Line: $scriptLineNumber\n"); append("Column: $scriptColumnNumber\n"); append("$message") })
    }
    // URArm Events
    data class Payload(val timestamp: ULong, val scriptName: String, val payLoad: Float): URScriptEvent()
    data class TcpOffset(val timestamp: ULong, val scriptName: String, val offset: Pose): URScriptEvent()
    data class PayloadCog(val timestamp: ULong, val scriptName: String, val cog: Vec3): URScriptEvent()
    data class PayloadInertia(val timestamp: ULong, val scriptName: String, val inertia: Inertia): URScriptEvent()
    // General Tool Events
    data class ToolDetection(val timestamp: ULong, val scriptName: String, val detected: Boolean): URScriptEvent()
    data class GripDetection(val timestamp: ULong, val scriptName: String, val detected: Boolean): URScriptEvent()
    // RG Tool Events
    data class WidthDepth(val timestamp: ULong, val scriptName: String, val width: Float, val depth: Float): URScriptEvent()
    // TFG Tool Events
    data class ExtIntWidth(val timestamp: ULong, val scriptName: String, val extWidth: Float, val intWidth: Float): URScriptEvent()
    // VG Tool Events
    data class ObjectDetection (val timestamp: ULong, val scriptName: String, val found: Boolean): URScriptEvent()
    data class VacuumAB (val timestamp: ULong, val scriptName: String, val vacuumA: Float, val vacuumB: Float): URScriptEvent()
    data class VacuumReached (val timestamp: ULong, val scriptName: String, val state: Boolean): URScriptEvent()
    data class VacuumReleased (val timestamp: ULong, val scriptName: String, val state: Boolean): URScriptEvent()


    companion object {

        fun fromKeyMessage(message: KeyMessage): URScriptEvent? {
            return when (message.robotMessageTitle) {
                "PROGRAM_XXX_STARTED" -> RunningState.START
                "PROGRAM_XXX_PAUSED" -> RunningState.PAUSED
                "PROGRAM_XXX_STOPPED" -> RunningState.END
                else -> return null
            }.let { state ->
                ExecutionState(
                    timestamp = message.timestamp,
                    scriptName = message.keyTextMessage,
                    state = state
                )
            }
        }

        fun fromSafetyModeMessage(message: SafetyModeMessage): URScriptEvent? {
            return when (message.safetyModeType) {
                SafetyModeType.SAFETY_MODE_ROBOT_EMERGENCY_STOP,
                SafetyModeType.SAFETY_MODE_SYSTEM_EMERGENCY_STOP,
                SafetyModeType.SAFETY_MODE_SAFEGUARD_STOP,
                SafetyModeType.SAFETY_MODE_PROTECTIVE_STOP -> message.safetyModeType
                else -> return null
            }.let { state ->
                SafetyModeStop(
                    timestamp = message.timestamp,
                    state = state
                )
            }
        }

        fun fromRuntimeExceptionMessage(message: RuntimeExceptionMessage): URScriptEvent {
            return RuntimeException(
                timestamp = message.timestamp,
                scriptColumnNumber = message.scriptColumnNumber,
                scriptLineNumber = message.scriptLineNumber,
                message = message.runtimeExceptionTextMessage
            )
        }


        fun fromTextMessage(message: TextMessage): URScriptEvent? {
            val tokens = parseTokens(message.textTextMessage,4) ?: return null
            if (tokens[0] != "event") return null
            val scriptName = tokens[1]
            val eventType = tokens[2]
            val eventValue = tokens[3]
            return parseTextMessage(message.timestamp, scriptName, eventType, eventValue)
        }

        private fun parseTokens(text: String, expectedTokens: Int): List<String>? {
            val tokens = text.split("||",ignoreCase= false,limit = expectedTokens)
            if (tokens.size != expectedTokens)
                return null
            return tokens
        }

        private fun parseTextMessage(timestamp: ULong, cmdName: String, eventType: String, eventValue: String): URScriptEvent? {
            return when (eventType.lowercase()) {
                "error" -> parseError(timestamp, cmdName, eventValue)
                "tcp_offset" -> parseTcpOffset(timestamp, cmdName, eventValue)
                "payload" -> parsePayload(timestamp, cmdName, eventValue)
                "payload_cog" -> parsePayloadCog(timestamp, cmdName, eventValue)
                "payload_inertia" -> parsePayloadInertia(timestamp, cmdName, eventValue)
                "tool_detection" -> parseToolDetection(timestamp, cmdName, eventValue)
                "grip_detection" -> parseGripDetection(timestamp, cmdName, eventValue)
                "ext_int_width" -> parseExtIntWidth(timestamp, cmdName, eventValue)
                "width_depth" -> parseWidthDepth(timestamp, cmdName, eventValue)
                "object_detection" -> parseObjectDetection(timestamp, cmdName, eventValue)
                "vacuum_ab" -> parseVacuumAB(timestamp, cmdName, eventValue)
                "vacuum_reached" -> parseVacuumReached(timestamp, cmdName, eventValue)
                "vacuum_released" -> parseVacuumReleased(timestamp, cmdName, eventValue)
                else -> null
            }
        }

        private fun parseError(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            val tokens = parseTokens(value,2) ?: return null
            return ScriptError(
                timestamp = timestamp,
                scriptName = cmdName,
                title = tokens[0],
                message = tokens[1]
            )
        }

        private fun parseTcpOffset(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            val expectedTokens = 6
            return parseTokens(value, expectedTokens)
                ?.mapNotNull(String::toDoubleOrNull)
                ?.takeIf { it.size == expectedTokens }
                ?.let { o ->
                    TcpOffset(
                        timestamp = timestamp,
                        scriptName = cmdName,
                        offset = Pose(o[0], o[1], o[2], o[3], o[4], o[5])
                    )
                }
        }

        private fun parsePayload(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            value.toFloatOrNull()?.let{ payload ->
                return Payload(
                    timestamp = timestamp,
                    scriptName = cmdName,
                    payLoad = payload
                    )
            }
            return null
        }

        private fun parsePayloadCog(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            val expectedTokens = 3
            return parseTokens(value, expectedTokens)
                ?.mapNotNull(String::toDoubleOrNull)
                ?.takeIf { it.size == expectedTokens }
                ?.let { c ->
                    PayloadCog(
                        timestamp = timestamp,
                        scriptName = cmdName,
                        cog = Vec3(c[0], c[1], c[2])
                    )
                }
        }

        private fun parsePayloadInertia(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            val expectedTokens = 6
            return parseTokens(value, expectedTokens)
                ?.mapNotNull(String::toFloatOrNull)
                ?.takeIf { it.size == expectedTokens }
                ?.let { i ->
                    PayloadInertia(
                        timestamp = timestamp,
                        scriptName = cmdName,
                        inertia = Inertia(i[0], i[1], i[2], i[3], i[4], i[5])
                    )
                }
        }

        private fun parseGripDetection(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            return when (value.lowercase()) {
                "true" -> GripDetection(timestamp, cmdName, true)
                "false" -> GripDetection(timestamp, cmdName, false)
                else -> null
            }
        }

        private fun parseObjectDetection(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            return when (value.lowercase()) {
                "true" -> ObjectDetection(timestamp, cmdName, true)
                "false" -> ObjectDetection(timestamp, cmdName, false)
                else -> null
            }
        }

        private fun parseVacuumReached(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            return when (value.lowercase()) {
                "true" -> VacuumReached(timestamp, cmdName, true)
                "false" -> VacuumReached(timestamp, cmdName, false)
                else -> null
            }
        }

        private fun parseVacuumReleased(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            return when (value.lowercase()) {
                "true" -> VacuumReleased(timestamp, cmdName, true)
                "false" -> VacuumReleased(timestamp, cmdName, false)
                else -> null
            }
        }

        private fun parseExtIntWidth(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            val expectedTokens = 2
            return parseTokens(value, expectedTokens)
                ?.mapNotNull(String::toFloatOrNull)
                ?.takeIf { it.size == expectedTokens }
                ?.let { w ->
                    ExtIntWidth(
                        timestamp = timestamp,
                        scriptName = cmdName,
                        extWidth = w[0],
                        intWidth = w[1]
                    )
                }
        }

        private fun parseWidthDepth(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            val expectedTokens = 2
            return parseTokens(value, expectedTokens)
                ?.mapNotNull(String::toFloatOrNull)
                ?.takeIf { it.size == expectedTokens }
                ?.let { wd ->
                    WidthDepth(
                        timestamp = timestamp,
                        scriptName = cmdName,
                        width = wd[0],
                        depth = wd[1]
                    )
                }
        }

        private fun parseToolDetection(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            return when (value.lowercase()) {
                "true" -> ToolDetection(timestamp, cmdName, true)
                "false" -> ToolDetection(timestamp, cmdName, false)
                else -> null
            }
        }

        private fun parseVacuumAB(timestamp: ULong, cmdName: String, value: String): URScriptEvent? {
            val expectedTokens = 2
            return parseTokens(value, expectedTokens)
                ?.mapNotNull(String::toFloatOrNull)
                ?.takeIf { it.size == expectedTokens }
                ?.let { v ->
                    VacuumAB(
                        timestamp = timestamp,
                        scriptName = cmdName,
                        vacuumA = v[0],
                        vacuumB = v[1]
                    )
                }
        }

    }
}



