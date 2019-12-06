package com.test.atlant.api.response.socket

data class X(
    val hash: String,
    val inputs: List<Input>,
    val lock_time: Int,
    val `out`: List<Out>,
    val relayed_by: String,
    val size: Int,
    val time: Long,
    val tx_index: Int,
    val ver: Int,
    val vin_sz: Int,
    val vout_sz: Int
)