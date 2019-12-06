package com.test.atlant

object Constants {
    const val BASE_URL = "https://api.dev.karta.com"
    const val SOCKET_URL = "wss://ws.blockchain.info/inv"

    const val UNCONFIRM_SUB_OP_VALUE = "unconfirmed_sub"
    const val UNCONFIRM_UNSUB_OP_VALUE = "unconfirmed_unsub"

    const val BTC_DIVIDER:Double = 100000000.0
    const val MILLISECONDS_ONE_SECOND = 1000
}

object PrefKeys {
    const val TOKEN = "token"
}

enum class NavigationScope{
    LOGIN,
    MAIN
}