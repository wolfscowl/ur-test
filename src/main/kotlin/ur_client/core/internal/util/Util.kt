package com.wolfscowl.ur_client.core.internal.util

import com.wolfscowl.ur_client.core.internal.config.Config
import com.wolfscowl.ur_client.core.internal.config.Default
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow

internal object Util {

    /**
     * Returns the current file in which the function was called.
     * For debugging purposes (see Log.d)
     * @return file name in which the function was called
     * */
    fun fileName(st: Int = 1): String {
        val fileName = Throwable().stackTrace[st].fileName
        return fileName?.substringBeforeLast(".") ?: "Unknown"
    }

    fun log(log: Any) {
        if (Config.SHOW_DEV_LOG)
            println(":::::[ ${fileName(2)} ]:::::\n" + log + "\n")
    }

    fun createUniqueScriptName(name: String): String {
        val formatter = DateTimeFormatter.ofPattern("ssmmhhddMMyyyy")
        val currentDate = LocalDateTime.now().format(formatter)
        val scriptName = name + currentDate
        return scriptName.take(30)
    }

    fun String.formatString(
        removeFirstLine: Boolean = true,
        additionalIndent: Int = 0
    ): String {
        val indent = " ".repeat(additionalIndent)
        val lines = this.lines()
        return lines
            .drop(if (removeFirstLine) 1 else 0)
            .joinToString("\n") { line -> indent + line }
    }

    fun Double.round(decimals: Int): Double {
        val factor = 10.0.pow(decimals)
        return kotlin.math.round(this * factor) / factor
    }

    fun Double.toStringFormat(decimalPlaces: Int = 2) = String.format("%.${decimalPlaces}f", this).replace(',', '.')

}

