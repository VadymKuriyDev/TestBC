package com.test.atlant.api

import com.auth0.android.jwt.JWT
import com.github.pwittchen.prefser.library.rx2.Prefser
import com.test.atlant.PrefKeys
import okhttp3.*
import timber.log.Timber

class TokenAuthenticator(private val accessTokenProvider: AccessTokenProvider) :
    Interceptor, Authenticator {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        var token = accessTokenProvider.getToken()
//        if (jwtToken.expiresAt?.before(Date(System.currentTimeMillis()))){
//            token = accessTokenProvider.getUpdatedToken()
//        }
//        if jwtToken.expiresAt  {
//            token = accessTokenProvider.getUpdatedToken()
//        }
        return if (token.isNotEmpty()){
            val jwtToken = JWT(token)
            val requestBuilder = original.newBuilder()
                .addHeader(AUTHORIZATION, token)
            chain.proceed(requestBuilder.build())
        }else {
            chain.proceed(original)
        }
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            Timber.d("Token expired, we will try update it")
            val token = accessTokenProvider.getUpdatedToken()
            response.request.newBuilder()
                .removeHeader(AUTHORIZATION)
                .addHeader(AUTHORIZATION, token)
                .build()
        }
        return null
    }

    companion object{
        const val AUTHORIZATION = "Authorization"
    }
}

interface AccessTokenProvider{
    fun getToken():String
    fun getUpdatedToken():String
    fun setToken(token: String)
    fun getSessionId():String?
}

class AccessTokenProviderImpl(private val prefser: Prefser, private val apiInterface: RefreshApiInterface) :AccessTokenProvider{

    override fun getSessionId(): String? {
        val token = getToken()
        val jwtToken = JWT(token)
        return jwtToken.getClaim("session_id").asString()
    }

    override fun setToken(token: String) {
        prefser.put(PrefKeys.TOKEN, token)
    }

    override fun getToken(): String {
        return prefser.get(PrefKeys.TOKEN, String::class.javaObjectType, "")
    }

    override fun getUpdatedToken(): String {
        val response = apiInterface.updateToken(getToken()).execute()
        response.body()?.let {
            prefser.put(PrefKeys.TOKEN, it.token)
            return it.token
        }
        return ""
    }

}