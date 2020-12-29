package com.sermarmu.breakingbad.feature.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sermarmu.breakingbad.R
import com.sermarmu.breakingbad.databinding.CharacterDetailFragmentBinding
import com.sermarmu.breakingbad.extensions.imageStatus
import com.sermarmu.breakingbad.extensions.loadImageFromDrawable
import com.sermarmu.breakingbad.extensions.loadImageFromUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment : BottomSheetDialogFragment() {

    private val args: CharacterDetailFragmentArgs by navArgs()

    private var _binding: CharacterDetailFragmentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = with(
        CharacterDetailFragmentBinding
            .inflate(layoutInflater)
    ) {
        _binding = this
        this.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            this.acivImageCharacterdetail.loadImageFromUrl(args.characterModel.img)
            this.tvNicknameCharacterDetail.text = args.characterModel.nickname
            this.tvNameCharacterDetail.text = args.characterModel.name
            this.tvBirthdayCharacterDetail.text = args.characterModel.birthday
            this.tvLastOccupationCharacterDetail.text = requireContext()
                .getString(
                    R.string.character_detail_occupation,
                    args.characterModel.occupation.last()
                )
            this.acivStatusCharacterdetail.loadImageFromDrawable(
                args.characterModel.imageStatus
            )
            this.tvPortrayedCharacterDetail.text = requireContext()
                .getString(
                    R.string.character_detail_portrayed,
                    args.characterModel.portrayed
                )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}