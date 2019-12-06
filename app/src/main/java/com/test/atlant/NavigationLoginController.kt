package com.test.atlant

import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.test.atlant.ui.FragmentUtils
import com.test.atlant.ui.activity.LoginActivity
import com.test.atlant.ui.activity.MainActivity
import com.test.atlant.ui.fragment.LoginFragment

class NavigationLoginController(private val activity: LoginActivity) {

    private var containerId: Int = R.id.content_container
    var fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToLogin() {
        if (fragmentManager.findFragmentByTag(LoginFragment::class.java.name) == null) {
            FragmentUtils.openContentFragment(fragmentManager, LoginFragment.newInstance(), containerId, false, LoginFragment::class.java.name, haveTransition = false)
        }
    }

    fun navigateToMain(){
        val mainIntent = Intent(activity, MainActivity::class.java)
        activity.startActivity(mainIntent)
        activity.finish()
    }
}