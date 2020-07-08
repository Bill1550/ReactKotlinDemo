package com.loneoaktech.tests.reactdemo.modules

import android.widget.Toast
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.loneoaktech.tests.reactdemo.data.NavAction
import timber.log.Timber

class AppNavigateModule(
    private val reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule() {

    override fun getName(): String = "AppNavigate"

    @ReactMethod
    fun navigate( link: String ) {
        // show a toast for now
        Timber.i("navigate to $link")
        Toast.makeText( reactContext, "Navigate to: $link", Toast.LENGTH_LONG ).show()

        NavigationMediator.postNavAction( NavAction(link) )
    }
}