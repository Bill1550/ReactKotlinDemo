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
import com.loneoaktech.tests.reactdemo.R
import com.loneoaktech.tests.reactdemo.data.NavAction
import com.loneoaktech.tests.reactdemo.di.ServiceLocator
import com.loneoaktech.tests.reactdemo.modules.NavigationMediator
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

        lifecycleScope.launchWhenResumed {
            NavigationMediator.navFlow.collect { na ->
                Timber.i("received nav action: $na")
                navigateToNavAction(na)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart, comp=${arguments?.getString(ARG_COMPONENT_NAME)}")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume, comp=${arguments?.getString(ARG_COMPONENT_NAME)}")
        reactInstanceManager.onHostResume( activity )


    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause, comp=${arguments?.getString(ARG_COMPONENT_NAME)}")
        reactInstanceManager.onHostPause( activity )
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop, comp=${arguments?.getString(ARG_COMPONENT_NAME)}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy, comp=${arguments?.getString(ARG_COMPONENT_NAME)}")
        (view as? ReactRootView)?.unmountReactApplication()?.let { Timber.i("React app unmounted") }
//        reactInstanceManager.onHostDestroy(activity)
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