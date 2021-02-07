package com.sermarmu.domain.model

import android.os.Parcelable
import com.sermarmu.data.entity.Character
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet

@Parcelize
data class CharacterModel(
    val charId: Int,
    val name: String,
    val birthday: String,
    val occupation: List<String>,
    val img: String,
    val status: Status,
    val appearance: List<Int>?,
    val nickname: String,
    val portrayed: String,
    var isFavourite: Boolean = false
) : Parcelable {
    enum class Status {
        ALIVE,
        DECEASED,
        PRESUME_DEAD,
        UNKNOWN,
    }
}

suspend fun Iterable<Character>.toCharactersModel() = asFlow().map {
    it.toCharacterModel()
}.toSet()

fun Character.toCharacterModel() =
    CharacterModel(
        charId = this.charId,
        name = this.name,
        birthday = this.birthday,
        occupation = this.occupation,
        img = this.img,
        status = when (this.status) {
            Character.Status.ALIVE -> CharacterModel.Status.ALIVE
            Character.Status.DECEASED -> CharacterModel.Status.DECEASED
            Character.Status.PRESUME_DEAD -> CharacterModel.Status.PRESUME_DEAD
            Character.Status.UNKNOWN -> CharacterModel.Status.UNKNOWN
        },
        appearance = this.appearance,
        nickname = this.nickname,
        portrayed = this.portrayed
    )