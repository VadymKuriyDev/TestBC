package com.test.atlant.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.atlant.Constants.UNCONFIRM_SUB_OP_VALUE
import com.test.atlant.Constants.UNCONFIRM_UNSUB_OP_VALUE
import com.test.atlant.NavigationScope
import com.test.atlant.data.MainRepository
import com.test.atlant.data.SocketRepository
import com.test.atlant.data.model.ProfileEntity
import com.test.atlant.data.model.Transaction
import com.tinder.scarlet.Event
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.State
import com.tinder.scarlet.WebSocket
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainViewModel(private val mainRepository: MainRepository,
                    private val socketRepository: SocketRepository) : RxViewModel() {

    private val _scope: MutableLiveData<NavigationScope> = MutableLiveData()
    val scope: LiveData<NavigationScope> get() = _scope

    private val _transactions: MutableLiveData<List<Transaction>> = MutableLiveData()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    private val _inValue: MutableLiveData<Double> = MutableLiveData(0.0)
    val inValue: LiveData<Double> get() = _inValue

    private val _outValue: MutableLiveData<Double> = MutableLiveData(0.0)
    val outValue: LiveData<Double> get() = _outValue

    private val _profile: MutableLiveData<ProfileEntity> = MutableLiveData()
    val profile: LiveData<ProfileEntity> get() = _profile

    init {
        compositeDisposable.add(
            socketRepository.observeEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ event ->
                    val description = when (event) {
                        is Event.OnLifecycle.StateChange<*> -> when (event.state) {
                            Lifecycle.State.Started -> "\uD83C\uDF1D On Lifecycle Start"
                            is Lifecycle.State.Stopped -> "\uD83C\uDF1A On Lifecycle Stop"
                            Lifecycle.State.Destroyed -> "\uD83D\uDCA5 On Lifecycle Terminate"
                        }
                        Event.OnLifecycle.Terminate -> "\uD83D\uDCA5 On Lifecycle Terminate"
                        is Event.OnWebSocket.Event<*> -> when (event.event) {
                            is WebSocket.Event.OnConnectionOpened<*> -> "\uD83D\uDEF0️ On WebSocket Connection Opened"
                            is WebSocket.Event.OnMessageReceived -> "\uD83D\uDEF0️ On WebSocket Message Received"
                            is WebSocket.Event.OnConnectionClosing -> "\uD83D\uDEF0️ On WebSocket Connection Closing"
                            is WebSocket.Event.OnConnectionClosed -> "\uD83D\uDEF0️ On WebSocket Connection Closed"
                            is WebSocket.Event.OnConnectionFailed -> "\uD83D\uDEF0️ On WebSocket Connection Failed"
                        }
                        Event.OnWebSocket.Terminate -> "\uD83D\uDEF0️ On WebSocket Terminate"
                        is Event.OnStateChange<*> -> when (event.state) {
                            is State.WaitingToRetry -> "\uD83D\uDCA4 WaitingToRetry"
                            is State.Connecting -> "⏳ Connecting"
                            is State.Connected -> "\uD83D\uDEEB Connected"
                            State.Disconnecting -> "⏳ Disconnecting"
                            State.Disconnected -> "\uD83D\uDEEC Disconnected"
                            State.Destroyed -> "\uD83D\uDCA5 Destroyed"
                        }
                        Event.OnRetry -> "⏰ On Retry"
                    }
                    Timber.d("Event data received $description")
                }, { throwable ->
                    Timber.e("observe error ${throwable.message} ")
                })
        )

        compositeDisposable.add(
            socketRepository.observeMessage()
                .subscribeOn(Schedulers.io())
                .map { transition ->
                    val inSum =  _transactions.value?.sumByDouble { it.input }?.also {
                        it + transition.input
                    }
                    val outSum  = _transactions.value?.sumByDouble { it.output }?.also {
                        it + transition.output
                    }
                    DataTransaction(inSum, outSum, transition)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ transaction ->
                    transaction?.transaction?.let{
                        val before:List<Transaction> = if (_transactions.value == null) {
                            emptyList()
                        }else{
                            _transactions.value!!
                        }
                        _transactions.value = mutableListOf(it).union(before).toList()
                    }
                    _outValue.value = transaction.outSum
                    _inValue.value = transaction.inSum
                    Timber.d("Message data received $transaction")
                }, { throwable ->
                    Timber.e("Message data error ${throwable.message} ")
                })
        )
    }

    fun subscribe() {
        sendMessage(SUBSCRIBE_MESSAGE)
    }

    fun unSubscribe() {
        sendMessage(UN_SUBSCRIBE_MESSAGE)
    }

    fun drop() {
        _transactions.value = emptyList()
        _outValue.value = 0.0
        _inValue.value = 0.0
    }

    private fun sendMessage(message: InputSocketMessage){
        Completable.fromAction {
            socketRepository.sendMessage(message)
        }
            .subscribeOn(Schedulers.computation())
            .subscribe()
    }

    fun updateProfileData(){
        compositeDisposable.add(mainRepository.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ profile ->
                _profile.value = profile
                Timber.d("User data received $profile")
            }) { throwable ->
                Timber.e("User data load error ${throwable.message} ")
            })
    }

    fun logout(){
        compositeDisposable.add(mainRepository.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ status ->
                Timber.d("Logout $status")
                _scope.value = NavigationScope.LOGIN
            }) { throwable ->
                Timber.e("Logout error ${throwable.message} ")
            })
    }

    companion object{
        private val SUBSCRIBE_MESSAGE = InputSocketMessage(UNCONFIRM_SUB_OP_VALUE)
        private val UN_SUBSCRIBE_MESSAGE = InputSocketMessage(UNCONFIRM_UNSUB_OP_VALUE)
    }
}

data class InputSocketMessage(val op:String)

data class DataTransaction(val inSum:Double?, val outSum:Double?, val transaction: Transaction)
