package com.wolfscowl.ur_client.model.element

import com.wolfscowl.ur_client.core.internal.util.Util.formatString


data class Error(
    val title: String,
    val message: String,
) {
    override fun toString(): String {
        return buildString {
            append("-=Error=-\n")
            append("$title\n")
            append(message.formatString(false,2))
        }
    }
}