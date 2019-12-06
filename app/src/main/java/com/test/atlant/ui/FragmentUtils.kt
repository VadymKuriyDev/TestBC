package com.test.atlant.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN

object FragmentUtils {

    @JvmOverloads
    fun openContentFragment(fragmentManager: FragmentManager, fragment: Fragment, containerViewId: Int,
                            addToBackStack: Boolean = true, tag: String? = null,
                            haveTransition: Boolean = true) {
        val transaction = fragmentManager.beginTransaction()
        if (fragmentManager.findFragmentById(containerViewId) == null) {
            transaction.add(containerViewId, fragment, tag)
        } else {
            transaction.replace(containerViewId, fragment, tag)
        }
        if (haveTransition) {
            transaction.setTransitionStyle(TRANSIT_FRAGMENT_FADE)
            transaction.setTransition(TRANSIT_FRAGMENT_OPEN)
        }
        if (addToBackStack) transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }
}
