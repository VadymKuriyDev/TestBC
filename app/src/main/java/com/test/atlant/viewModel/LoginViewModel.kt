package com.test.atlant.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.atlant.NavigationScope
import com.test.atlant.data.LoginRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LoginViewModel(private val loginRepository: LoginRepository) : RxViewModel() {

    private val _scope: MutableLiveData<NavigationScope> = MutableLiveData(NavigationScope.LOGIN)
    val scope: LiveData<NavigationScope> get() = _scope

    private val _loginEnable: MutableLiveData<Boolean> = MutableLiveData(false)
    val loginEnable: LiveData<Boolean> get() = _loginEnable

    fun changeLoginState(email:String, password: String){
        _loginEnable.value = email.isNotEmpty() && password.isNotEmpty()
    }

    fun login(email:String, password: String){
        compositeDisposable.add(loginRepository.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _scope.value = NavigationScope.MAIN
            }) { throwable ->
                Timber.e("Load  error ${throwable.message} ")
            })
    }
}
