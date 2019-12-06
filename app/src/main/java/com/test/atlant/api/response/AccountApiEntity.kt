package com.test.atlant.api.response

data class AccountApiEntity(
    val `2fa_method`: Any,
    val account_id: String,
    val account_type: String,
    val created_at: String,
    val email: String,
    val email_verified: Boolean,
    val password: String,
    val phone: String,
    val totp_verified: Boolean
)