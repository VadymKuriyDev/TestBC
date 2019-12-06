package com.test.atlant.api

import com.test.atlant.api.response.socket.SocketMessageApiEntity
import com.test.atlant.viewModel.InputSocketMessage
import com.tinder.scarlet.Event
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface WebsocketAtlantService {

    @Receive
    fun observeWebSocketEvent(): Flowable<Event>

    @Receive
    fun observeMessage(): Flowable<SocketMessageApiEntity>

    @Send
    fun sendText(messageInput: InputSocketMessage): Boolean
}