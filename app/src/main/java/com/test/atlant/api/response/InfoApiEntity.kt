package com.test.atlant.api.response

data class InfoApiEntity(
    val account: AccountApiEntity,
    val profiles: List<ProfileApiEntity>,
    val session: Any
)