package com.sermarmu.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), CoroutineScope {
    override lateinit var coroutineContext: CoroutineContext

    override fun onCreate(savedInstanceState: Bundle?) {
        coroutineContext = SupervisorJob() + Dispatchers.Main
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }
}