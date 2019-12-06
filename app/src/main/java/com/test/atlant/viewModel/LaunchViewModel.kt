package com.test.atlant.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.pwittchen.prefser.library.rx2.Prefser
import com.test.atlant.NavigationScope
import com.test.atlant.PrefKeys

class LaunchViewModel(prefs: Prefser) : RxViewModel() {

    private val _scope: MutableLiveData<NavigationScope>
    val scope: LiveData<NavigationScope> get() = _scope

    init {
        val navScope = if (prefs.get(PrefKeys.TOKEN, String::class.javaObjectType, "").isNotEmpty()) {
            NavigationScope.MAIN
        }else{
            NavigationScope.LOGIN
        }
        _scope = MutableLiveData(navScope)
    }
}
