package com.wolfscowl.ur_client.core.internal.decoder

import com.wolfscowl.ur_client.core.internal.decoder.program_decoder.ProgramDecoder

// sub type of main message: PROGRAM_STATE
internal enum class ProgramStateMessageType(val code: Int,val decoder: ProgramDecoder?) {
    GLOBAL_VARIABLES_SETUP(0, null),
    GLOBAL_VARIABLES_UPDATE(1, null),
    UNDEFINED(-9999, null);

    companion object {
        fun fromCode(code: Int): ProgramStateMessageType = entries.find { it.code == code } ?: UNDEFINED
    }
}
