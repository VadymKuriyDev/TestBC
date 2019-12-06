package com.test.atlant.viewModel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class RxViewModel: ViewModel() {

    protected var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.takeIf { !it.isDisposed }?.apply { dispose() }
    }
}