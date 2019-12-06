package com.test.atlant

import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.test.atlant.ui.FragmentUtils
import com.test.atlant.ui.activity.LoginActivity
import com.test.atlant.ui.activity.MainActivity
import com.test.atlant.ui.fragment.MainFragment

class NavigationMainController(private val activity: MainActivity) {

    private var containerId: Int = R.id.content_container
    var fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToMain() {
        if (fragmentManager.findFragmentByTag(MainFragment::class.java.name) == null) {
            FragmentUtils.openContentFragment(fragmentManager, MainFragment.newInstance(), containerId, false, MainFragment::class.java.name, haveTransition = false)
        }
    }

    fun navigateToLogin(){
        val loginIntent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(loginIntent)
        activity.finish()
    }
}