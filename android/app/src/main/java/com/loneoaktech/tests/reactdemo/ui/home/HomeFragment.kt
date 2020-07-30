package com.loneoaktech.tests.reactdemo.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.loneoaktech.tests.reactdemo.MyReactActivity
import com.loneoaktech.tests.reactdemo.R
import com.loneoaktech.tests.reactdemo.TestActivity
import timber.log.Timber

/**
 * A simple home fragment.
 * It contains a button to launch RN inside a separate activity.
 */
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        root.findViewById<Button>(R.id.launchReactButton).apply {
            setOnClickListener {
                Timber.i("Launching MyReactActivity")
                findNavController().navigate(R.id.action_navigation_home_to_myReactActivity)
            }
        }

        return root
    }


}