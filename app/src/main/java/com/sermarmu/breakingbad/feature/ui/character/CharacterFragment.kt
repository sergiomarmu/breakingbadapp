package com.sermarmu.breakingbad.feature.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.sermarmu.breakingbad.R
import com.sermarmu.breakingbad.databinding.CharacterFragmentBinding
import com.sermarmu.breakingbad.extensions.loadImageFromUrl
import com.sermarmu.core.base.BaseFragment
import com.sermarmu.domain.model.CharacterModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.character_model.view.*
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
            binding.srlCharacters.apply {
                isRefreshing = true
                setOnRefreshListener {
                    viewModel.onUserRefreshAction()
                }
            }
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
                { onCharacterState(it) }
            )
    }

    private fun onCharacterState(state: CharacterViewModel.CharacterState) {
        when (state) {
            is CharacterViewModel.CharacterState.Success -> {
                binding.srlCharacters.isRefreshing = false
                ((binding.rvCharacters.adapter as ConcatAdapter).adapters[1] as Adapter)
                    .submitList(state.characters.toList())
            }
            is CharacterViewModel.CharacterState.Failure -> {
                binding.srlCharacters.isRefreshing = false
                when (state) {
                    is CharacterViewModel.CharacterState.Failure.NoInternet ->
                        Snackbar.make(
                            requireView(),
                            state.message ?: getString(R.string.error_no_internet_connection),
                            Snackbar.LENGTH_LONG
                        ).show()
                    is CharacterViewModel.CharacterState.Failure.Unexpected ->
                        Snackbar.make(
                            requireView(),
                            state.message ?: getString(R.string.error_unknown),
                            Snackbar.LENGTH_LONG
                        ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    class HeaderAdapter: RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {
        class ViewHolder(
            parent:ViewGroup
        ): RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.character_header_adapter,
                    parent,
                    false
                )
        )

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder = ViewHolder(parent)

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) = Unit

        override fun getItemCount(): Int = 1
    }

    class Adapter(
        val listener: (CharacterModel) -> Unit
    ): ListAdapter<CharacterModel, Adapter.ViewHolder>(
        AdapterDiffCallback
    ) {
        class ViewHolder(
            v: View
        ): RecyclerView.ViewHolder(v) {
            fun bind(
                model: CharacterModel,
                listener: (CharacterModel) -> Unit
            ) = with(itemView) {
                mtv_name_charactermodel.text = model.name
                mtv_status_charactermodel.text = model.status.toString()
                aciv_image_charactermodel.loadImageFromUrl(
                    model.img
                )
                setOnClickListener {
                    listener(model)
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.character_model,
                    parent,
                    false
                )
        )

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            getItem(position)!!.also {
                holder.bind(it, listener)
            }
        }

        private object AdapterDiffCallback : DiffUtil.ItemCallback<CharacterModel>() {
            override fun areItemsTheSame(
                oldItem: CharacterModel,
                newItem: CharacterModel
            ): Boolean = oldItem.charId == newItem.charId

            override fun areContentsTheSame(
                oldItem: CharacterModel,
                newItem: CharacterModel
            ): Boolean = oldItem.charId == newItem.charId &&
                    oldItem.name == newItem.name &&
                    oldItem.img == newItem.img &&
                    oldItem.status == newItem.status
        }
    }

}