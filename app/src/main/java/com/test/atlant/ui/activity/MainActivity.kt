package com.test.atlant.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.atlant.NavigationMainController
import com.test.atlant.R
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val navigationMainController: NavigationMainController by inject{parametersOf(this@MainActivity)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationMainController.navigateToMain()
    }
}
