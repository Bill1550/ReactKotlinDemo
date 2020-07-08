package com.loneoaktech.tests.reactdemo.ui.dashboard

import androidx.navigation.fragment.findNavController
import com.loneoaktech.tests.reactdemo.R
import com.loneoaktech.tests.reactdemo.data.NavAction

class ReactNotificationsFragment : ReactBaseFragment() {


    override fun getNavActionId(navAction: NavAction): Int? =
        when(navAction.url) {

            "/home" -> R.id.action_navigation_notifications_to_navigation_home
            else -> null
        }

}