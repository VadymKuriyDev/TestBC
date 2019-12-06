package com.test.atlant.data

import com.test.atlant.Constants.BTC_DIVIDER
import com.test.atlant.Constants.MILLISECONDS_ONE_SECOND
import com.test.atlant.api.response.LoginResponse
import com.test.atlant.api.response.ProfileApiEntity
import com.test.atlant.api.response.ProfileApiResponse
import com.test.atlant.api.response.socket.SocketMessageApiEntity
import com.test.atlant.data.model.ProfileEntity
import com.test.atlant.data.model.Transaction
import java.util.*

object DataMapping {

    fun fromApiToModel(loginResponse: LoginResponse): String {
        return loginResponse.token
    }

    fun fromApiToModel(profileApiResponse: ProfileApiResponse): ProfileEntity {
        val profileApi = profileApiResponse.info.profiles.firstOrNull()
        profileApi?.let {
            return ProfileEntity(it.profile_type, it.first_name, it.last_name, it.avatar_url)
        }
        return ProfileEntity.EMPTY_PROFILE
    }

    fun fromApiToModel(websocketApiEntity: SocketMessageApiEntity): Transaction {
        val hash = websocketApiEntity.x.hash
        val date = Date(websocketApiEntity.x.time * MILLISECONDS_ONE_SECOND)
        val input: Double = websocketApiEntity.x.inputs.sumBy { it.prev_out.value }.div(BTC_DIVIDER)
        val output = websocketApiEntity.x.out.sumBy { it.value }.div(BTC_DIVIDER)

        return Transaction(hash, input, output, date, System.currentTimeMillis())
    }
}