package com.test.atlant.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.atlant.NavigationLoginController
import com.test.atlant.R
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class LoginActivity : AppCompatActivity() {

    private val navigationLoginController: NavigationLoginController by inject{parametersOf(this@LoginActivity)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationLoginController.navigateToLogin()
    }
}
