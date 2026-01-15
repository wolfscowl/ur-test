package com.wolfscowl.ur_client.test

fun main() {
    val value = "hallo"
    val tokens = value.split("::",ignoreCase= false,limit = 3)
    println("Size: ${tokens.size}")
    println(tokens[0])
}