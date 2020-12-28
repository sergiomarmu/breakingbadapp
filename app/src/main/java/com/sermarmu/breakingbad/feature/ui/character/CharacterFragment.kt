package com.sermarmu.breakingbad.feature.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sermarmu.breakingbad.databinding.CharacterFragmentBinding
import com.sermarmu.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFragment : BaseFragment() {

    private var _binding: CharacterFragmentBinding? = null
    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = with(
        CharacterFragmentBinding.inflate(layoutInflater)
    ) {
        _binding = this
        this.root
    }
}