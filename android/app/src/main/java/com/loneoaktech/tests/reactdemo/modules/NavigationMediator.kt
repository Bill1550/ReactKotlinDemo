package com.loneoaktech.tests.reactdemo.modules

import com.loneoaktech.tests.reactdemo.data.NavAction
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow

/**
 * A simple hub to forward nav requests received from RN modules.
 * Implemented as a singleton.
 *
 * Nav events are routed through this singleton because the React code is instancing the AppNavigationModule
 * and it isn't guaranteed to be a singleton.
 *
 */
object NavigationMediator {

    private val navChannel = BroadcastChannel<NavAction>(1)

    val navFlow = navChannel.asFlow()

    fun postNavAction( navAction: NavAction ) {
        navChannel.offer( navAction )
    }
}