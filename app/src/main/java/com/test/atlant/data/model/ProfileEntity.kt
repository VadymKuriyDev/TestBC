package com.test.atlant.data.model

data class ProfileEntity(val profileType: String,
                         val firstName: String,
                         val lastName: String,
                         val imageUrl: String){

    companion object {
        val EMPTY_PROFILE = ProfileEntity("", "","","")
    }
}