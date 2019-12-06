package com.test.atlant.api.response

data class ProfileApiEntity(
    val account_id: String,
    val avatar_url: String,
    val email: String,
    val first_name: String,
    val gender: Any,
    val joined_at: String,
    val kyc_verified: Boolean,
    val langs_spoken_names: List<Any>,
    val last_name: String,
    val location: String,
    val phone_country: Any,
    val phone_number: Any,
    val profile_id: String,
    val profile_type: String
)