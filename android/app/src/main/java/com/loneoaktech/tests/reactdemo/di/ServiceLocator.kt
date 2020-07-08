package com.loneoaktech.tests.reactdemo.di

import android.app.Application
import android.content.Context
import com.facebook.react.ReactInstanceManager
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import com.loneoaktech.tests.reactdemo.BuildConfig
import com.loneoaktech.tests.reactdemo.modules.NavigateModulesPackage

class ServiceLocator private constructor(
    private val application: Application
) {

    companion object {
        private var serviceLocator: ServiceLocator? = null

        fun instance( context: Context): ServiceLocator {
            return synchronized(this) {
                serviceLocator ?: let {
                    ServiceLocator( context.applicationContext as Application).also {
                        serviceLocator = it
                    }
                }
            }
        }
    }


    val reactInstanceManager: ReactInstanceManager by lazy {
        ReactInstanceManager.builder()
            .setApplication( application )
//            .setCurrentActivity( this ) //-- since they specify the Instance Manager should be a singleton, this smells like a leak.
            .setBundleAssetName("index.android.bundle")
            .setJSMainModulePath("index")
            .addPackages( listOf( MainReactPackage(), NavigateModulesPackage() ) )
            .setUseDeveloperSupport( BuildConfig.DEBUG )
            .setInitialLifecycleState( LifecycleState.BEFORE_RESUME )
            .build()
    }



}