package com.test.atlant.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.test.atlant.NavigationScope
import com.test.atlant.R
import com.test.atlant.viewModel.LaunchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LaunchActivity : AppCompatActivity() {

    private val launchViewModel: LaunchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchViewModel.scope.observe(this, Observer { scope ->
            scope?.let {
                val flowIntent = when (it) {
                    NavigationScope.MAIN -> Intent(this, MainActivity::class.java)
                    NavigationScope.LOGIN -> Intent(this, LoginActivity::class.java)
                }
                startActivity(flowIntent)
                finish()
            }
        })
    }
}
