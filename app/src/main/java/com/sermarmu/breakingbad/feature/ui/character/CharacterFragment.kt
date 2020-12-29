package com.sermarmu.breakingbad.feature.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.sermarmu.breakingbad.databinding.CharacterFragmentBinding
import com.sermarmu.breakingbad.feature.ui.character.adapter.Adapter
import com.sermarmu.breakingbad.feature.ui.character.adapter.HeaderAdapter
import com.sermarmu.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CharacterFragment : BaseFragment() {

   private val viewModel: CharacterViewModel by viewModels()

    private var _binding: CharacterFragmentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = with(
        CharacterFragmentBinding
            .inflate(layoutInflater)
    ) {
        _binding = this
        this.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCharacters.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ConcatAdapter(
                HeaderAdapter(),
                Adapter {
                    navController.navigate(
                        CharacterFragmentDirections.actNavDestCharacterDetail(it)
                    )
                }
            )
        }

        viewModel.characterStateLiveData
            .observe(
                viewLifecycleOwner,
                Observer { state ->
                    when (state) {
                        is CharacterViewModel.CharacterState.Success ->
                            ((binding.rvCharacters.adapter as ConcatAdapter).adapters[1] as Adapter)
                                .submitList(state.characters.toList())
                        is CharacterViewModel.CharacterState.Failure ->
                            when (state) {
                                is CharacterViewModel.CharacterState.Failure.NoInternet -> {}
                                is CharacterViewModel.CharacterState.Failure.Unexpected -> {}
                            }
                    }

                }
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}