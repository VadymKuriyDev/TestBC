package com.test.atlant.data

import com.google.gson.Gson
import com.test.atlant.api.AccessTokenProvider
import com.test.atlant.api.AccountApiInterface
import com.test.atlant.api.WebsocketAtlantService
import com.test.atlant.api.response.body.LoginBody
import com.test.atlant.api.response.body.LogoutBody
import com.test.atlant.data.model.ProfileEntity
import com.test.atlant.data.model.Transaction
import com.test.atlant.viewModel.InputSocketMessage
import com.tinder.scarlet.Event
import io.reactivex.Flowable
import io.reactivex.Single

interface LoginRepository{
    fun login(email: String, password: String):Single<Boolean>
}

interface MainRepository{
    fun getProfile():Single<ProfileEntity>
    fun logout():Single<Boolean>
}

interface SocketRepository{
    fun observeEvent():Flowable<Event>
    fun observeMessage():Flowable<Transaction>
    fun sendMessage(messageInput: InputSocketMessage):Boolean
}

class LoginRepositoryImpl(private val apiInterface: AccountApiInterface, private val tokenAuth: AccessTokenProvider): LoginRepository {

    override fun login(email: String, password: String): Single<Boolean> {
        val loginBody = LoginBody(email, password)
        return apiInterface.login(Gson().toJson(loginBody))
            .map { DataMapping.fromApiToModel(it) }
            .flatMap {
                tokenAuth.setToken(it)
                return@flatMap Single.just(true)
            }
    }
}

class MainRepositoryImpl(private val apiInterface: AccountApiInterface, private val tokenAuth: AccessTokenProvider): MainRepository {

    override fun logout(): Single<Boolean> {
        val logoutBody = LogoutBody(tokenAuth.getSessionId())
        return apiInterface.logout(Gson().toJson(logoutBody))
            .flatMap {
                tokenAuth.setToken("")
                return@flatMap Single.just(true)
            }
    }

    override fun getProfile(): Single<ProfileEntity> {
        return apiInterface.getProfile()
            .map { DataMapping.fromApiToModel(it) }
    }
}

class SocketRepositoryImpl(private val websocketAtlantService: WebsocketAtlantService): SocketRepository{

    override fun observeMessage(): Flowable<Transaction> {
        return websocketAtlantService.observeMessage()
            .map { DataMapping.fromApiToModel(it) }
    }

    override fun sendMessage(messageInput: InputSocketMessage): Boolean {
        return websocketAtlantService.sendText(messageInput)
    }

    override fun observeEvent(): Flowable<Event> {
        return  websocketAtlantService.observeWebSocketEvent()
    }
}