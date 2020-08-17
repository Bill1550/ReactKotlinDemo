package com.loneoaktech.tests.reactdemo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.Arguments
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.loneoaktech.tests.reactdemo.R
import com.loneoaktech.tests.reactdemo.data.NavAction
import com.loneoaktech.tests.reactdemo.di.ServiceLocator
import com.loneoaktech.tests.reactdemo.modules.NavigationMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import timber.log.Timber


/**
 * Base fragment to hold the RN view.
 */
abstract class ReactBaseFragment : Fragment() {

    companion object {
        const val ARG_COMPONENT_NAME = "arg_component_name"

        fun arguments( componentName: String ): Bundle = Bundle().apply {
            putString( ARG_COMPONENT_NAME, componentName )
        }
    }

    private lateinit var reactInstanceManager: ReactInstanceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // do the injecting here
        reactInstanceManager = ServiceLocator.instance(requireContext()).reactInstanceManager
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        return ReactRootView( requireContext() ).also { it.startReactApplication( reactInstanceManager,
            getComponentName() )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Launch a job to list to nav requests from RN
        lifecycleScope.launchWhenResumed {
            NavigationMediator.navFlow.collect { na ->
                Timber.i("received nav action: $na")
                navigateToNavAction(na)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart, comp=${getComponentName()}")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume, comp=${getComponentName()}")
        reactInstanceManager.onHostResume( activity )

        Timber.i(" emitter: ${reactInstanceManager.currentReactContext?.getJSModule( DeviceEventManagerModule.RCTDeviceEventEmitter::class.java )?.javaClass?.simpleName}")

        lifecycleScope.launchWhenResumed {
            delay(500)
            val emitter = reactInstanceManager.currentReactContext?.getJSModule( DeviceEventManagerModule.RCTDeviceEventEmitter::class.java )
            emitter?.let { e ->
                val args = Arguments.createMap()
                args.putString("key1", "value1")
                e.emit("AndroidMsg", args)
                Timber.i("Message sent to react")
            } ?: Timber.e("Emitter not available")
    }
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause, comp=${getComponentName()}")
        reactInstanceManager.onHostPause( activity )
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop, comp=${getComponentName()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy, comp=${getComponentName()}")
        (view as? ReactRootView)?.unmountReactApplication()?.let { Timber.i("React app unmounted") }
//        reactInstanceManager.onHostDestroy(activity) - onHostDestroy needs to happen at the activity level
        // not the fragment level.  RN isn't maintaining state per fragment
    }

    open fun getComponentName(): String {
        return  arguments?.getString(ARG_COMPONENT_NAME) ?: "MyReactNativeApp"
    }

    /**
     * Derived class should implement nav to the specified destination.
     * Allows nav to be origin specific as done by the AndroidX Navigation Lib.
     */
    open fun navigateToNavAction(navAction: NavAction) {
       getNavActionId(navAction)?.let { action ->
            findNavController().navigate(action)
        }
    }

    open fun getNavActionId( navAction: NavAction ): Int? = null

}