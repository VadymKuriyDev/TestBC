package com.test.atlant.di

import android.app.Application
import com.github.pwittchen.prefser.library.rx2.Prefser
import com.google.gson.GsonBuilder
import com.test.atlant.BuildConfig
import com.test.atlant.Constants
import com.test.atlant.Constants.SOCKET_URL
import com.test.atlant.api.*
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


val retrofitModule = module {

    single {
        retrofit(Constants.BASE_URL, get())
    }

    single<AccessTokenProvider> {  AccessTokenProviderImpl(get(), get()) }

    factory<RefreshApiInterface> {
        provideRetrofitForRefresh(Constants.BASE_URL).create(RefreshApiInterface::class.java)
    }

    factory<AccountApiInterface> {
        get<Retrofit>().create(AccountApiInterface::class.java)
    }

    single {
        providePrefser(androidApplication())
    }

    single {
        provideWebSocket()
    }
}

private fun providePrefser(app: Application): Prefser = Prefser(app)

private val interceptor: Interceptor
    get() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

//This retrofit provider is using only for refreshing token
private fun provideRetrofitForRefresh(baseUrl: String)= Retrofit.Builder()
    .client(OkHttpClient.Builder().build())
    .baseUrl(baseUrl)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(gson()))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

private fun retrofit(baseUrl: String, authProvider: AccessTokenProvider) = Retrofit.Builder()
    .client(okhttp(authProvider))
    .baseUrl(baseUrl)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(gson()))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

private fun okhttp(authProvider: AccessTokenProvider): OkHttpClient{
    val builder = OkHttpClient().newBuilder()
    builder.addInterceptor(interceptor)
    builder.authenticator(TokenAuthenticator(authProvider))
    builder.addInterceptor(TokenAuthenticator(authProvider))
    return builder.build()
}

private fun gson() = GsonBuilder()
    .setLenient()
    .create()

private fun provideWebSocket(): WebsocketAtlantService {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    val okHttp = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()
    val scarlet = Scarlet.Builder()
        .webSocketFactory(okHttp.newWebSocketFactory(SOCKET_URL))
        .addMessageAdapterFactory(GsonMessageAdapter.Factory())
        .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
        .build()
    return scarlet.create()
}