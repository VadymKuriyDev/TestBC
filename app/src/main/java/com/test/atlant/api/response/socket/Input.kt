package com.test.atlant.api.response.socket

data class Input(
    val prev_out: PrevOut,
    val script: String,
    val sequence: Long
)