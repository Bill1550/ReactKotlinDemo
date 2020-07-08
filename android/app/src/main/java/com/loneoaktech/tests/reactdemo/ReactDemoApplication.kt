package com.loneoaktech.tests.reactdemo

import android.app.Application
import com.facebook.soloader.SoLoader
import timber.log.Timber

class ReactDemoApplication : Application() /*, ReactApplication */ {

    init {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

//    private val reactNativeHost = object: ReactNativeHost(this) {
//
//        override fun getJSMainModuleName(): String = "index"
//
//        override fun getPackages(): MutableList<ReactPackage> {
//            return Arrays.asList(
//                MainReactPackage()
//            )
//        }
//
//        override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG
//
//    }

//    override fun getReactNativeHost(): ReactNativeHost = reactNativeHost


    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate!")
        SoLoader.init(this, false)
    }

}