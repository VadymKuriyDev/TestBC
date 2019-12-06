package com.test.atlant.api.response.socket

data class Out(
    val addr: String,
    val n: Int,
    val script: String,
    val spent: Boolean,
    val tx_index: Int,
    val type: Int,
    val value: Int
)