package com.loneoaktech.tests.reactdemo

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.ReactRootView
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.loneoaktech.tests.reactdemo.di.ServiceLocator
import timber.log.Timber

class MyReactActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {

    companion object {
        private const val OVERLAY_PERMISSION_REQ_CODE = 142
    }

    private val reactRootView by lazy { ReactRootView(this) }

    // home brew inject
    private val reactInstanceManager by lazy { ServiceLocator.instance(this).reactInstanceManager }

//    private val reactInstanceManager by lazy {
//        ReactInstanceManager.builder()
//            .setApplication( application )
////            .setCurrentActivity( this ) //-- since they specify the Instance Manager should be a singleton, this smells like a leak.
//            .setBundleAssetName("index.android.bundle")
//            .setJSMainModulePath("index")
//            .addPackage( MainReactPackage() )
//            .setUseDeveloperSupport( BuildConfig.DEBUG )
//            .setInitialLifecycleState( LifecycleState.BEFORE_RESUME )
//            .build()
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")

        reactRootView.startReactApplication( reactInstanceManager, "MyReactNativeApp", null)
        setContentView( reactRootView )


        // TODO reimplement w/ Easy Permissions
        if (Build.VERSION.SDK_INT >= 23){
            if ( !Settings.canDrawOverlays(this)) {
                Timber.i("need to ask for permission")
                startActivityForResult(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    ),
                    OVERLAY_PERMISSION_REQ_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE ) {
            if ( Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)){
                Log.w("MyReactActivity", "Overlay permission not granted")
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)

        reactInstanceManager.onActivityResult(this, requestCode, resultCode, data )
//        super.onActivityResult(requestCode, resultCode, data)
    }



    override fun onPause() {
        super.onPause()
        reactInstanceManager.onHostPause(this)
    }

    override fun onResume() {
        super.onResume()

        reactInstanceManager.onHostResume(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        reactInstanceManager.onHostDestroy(this)
        reactRootView.unmountReactApplication()
    }

    override fun invokeDefaultOnBackPressed() {
        Timber.i("invokeDefaultOnBackPressed")
        super.onBackPressed()
    }

    override fun onBackPressed() {
        Timber.i("onBackPressed")
//        reactInstanceManager.onBackPressed()  -- not working
        super.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_MENU ) {
            reactInstanceManager.showDevOptionsDialog()
            true
        } else
            super.onKeyUp(keyCode, event)
    }
}