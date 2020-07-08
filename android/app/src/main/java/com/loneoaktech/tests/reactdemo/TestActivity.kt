package com.loneoaktech.tests.reactdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class TestActivity : AppCompatActivity() {

    init {
        Timber.i("ctor")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState )

        Timber.i("onCreate")
        setContentView(R.layout.activity_test)
    }
}