package com.sermarmu.breakingbad

import android.os.Bundle
import com.sermarmu.breakingbad.databinding.ActivityMainBinding
import com.sermarmu.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding
            .inflate(layoutInflater)
            .also {
                binding = it
                setContentView(it.root)
            }
    }
}