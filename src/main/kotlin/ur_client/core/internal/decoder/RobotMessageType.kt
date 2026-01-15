package com.wolfscowl.ur_client.core.internal.decoder

import com.wolfscowl.ur_client.core.internal.decoder.message_decoder.KeyDecoder
import com.wolfscowl.ur_client.core.internal.decoder.message_decoder.MessageDecoder
import com.wolfscowl.ur_client.core.internal.decoder.message_decoder.PopupDecoder
import com.wolfscowl.ur_client.core.internal.decoder.message_decoder.ProgramThreadDecoder
import com.wolfscowl.ur_client.core.internal.decoder.message_decoder.RequestValueDecoder
import com.wolfscowl.ur_client.core.internal.decoder.message_decoder.RobotCommDecoder
import com.wolfscowl.ur_client.core.internal.decoder.message_decoder.RuntimeExceptionDecoder
import com.wolfscowl.ur_client.core.internal.decoder.message_decoder.SafetyModeDecoder
import com.wolfscowl.ur_client.core.internal.decoder.message_decoder.TextDecoder
import com.wolfscowl.ur_client.core.internal.decoder.message_decoder.VersionDecoder


// sub type of main message: ROBOT_MESSAGE
internal enum class RobotMessageType(val code: Int, val decoder: MessageDecoder?) {
    VERSION(3, VersionDecoder),
    SAFETY_MODE(5, SafetyModeDecoder),
    ROBOT_COMM(6, RobotCommDecoder), // alias ERROR_CODE
    KEY(7, KeyDecoder),
    POPUP(2, PopupDecoder),
    PROGRAM_THREAD(14, ProgramThreadDecoder),
    REQUEST_VALUE(9, RequestValueDecoder),
    TEXT(0, TextDecoder),
    RUNTIME_EXCEPTION(10, RuntimeExceptionDecoder),
    UNDEFINED(-9999, null);               // Code 12,

    companion object {
        fun fromCode(code: Int): RobotMessageType = entries.find { it.code == code } ?: UNDEFINED
    }
}
