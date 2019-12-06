package com.test.atlant.api

import com.test.atlant.api.TokenAuthenticator.Companion.AUTHORIZATION
import com.test.atlant.api.response.LoginResponse
import com.test.atlant.api.response.LogoutApiResponse
import com.test.atlant.api.response.ProfileApiResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountApiInterface {
    /**
     * @return user token
     */
    @POST("accounts/auth")
    fun login(@Body loginBody: String): Single<LoginResponse>

    @POST("accounts/sessions/end")
    fun logout(@Body logoutBody: String): Single<LogoutApiResponse>

    /**
     * @return user data
     */
    @GET("accounts/current")
    fun getProfile(): Single<ProfileApiResponse>

}

interface RefreshApiInterface {
    /**
     * @return new token
     */
    @POST("accounts/sessions/refresh")
    fun updateToken(@Header(AUTHORIZATION) token: String): Call<LoginResponse>
}