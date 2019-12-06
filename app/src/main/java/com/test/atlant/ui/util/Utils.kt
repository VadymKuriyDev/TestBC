package com.test.atlant.ui.util

import android.annotation.SuppressLint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.toSimpleString() : String {
    val format = SimpleDateFormat("hh:mm")
    return format.format(this)
}

fun Double.toBTCString() : String {
    val nf = NumberFormat.getNumberInstance()
    nf.maximumFractionDigits = 8
    nf.minimumFractionDigits = 8
    return nf.format(this)
}