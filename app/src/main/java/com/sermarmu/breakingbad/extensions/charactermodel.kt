package com.sermarmu.breakingbad.extensions

import com.sermarmu.breakingbad.R
import com.sermarmu.domain.model.CharacterModel

val CharacterModel.imageStatus : Int
get() = when (this.status) {
    CharacterModel.Status.ALIVE -> R.drawable.ic_alive_24px
    CharacterModel.Status.DECEASED -> R.drawable.ic_deceased_24px
    CharacterModel.Status.PRESUME_DEAD -> R.drawable.ic_presume_dead_24px
    CharacterModel.Status.UNKNOWN -> R.drawable.ic_presume_dead_24px
}