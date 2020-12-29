package com.sermarmu.data.entity

import com.sermarmu.data.source.network.io.CharacterOutput
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet

data class Character(
    val charId: Int,
    val name: String,
    val birthday: String,
    val occupation: List<String>,
    val img: String,
    val status: Status,
    val appearance: List<Int>?,
    val nickname: String,
    val portrayed: String
) {
    enum class Status {
        ALIVE,
        DECEASED,
        PRESUME_DEAD,
        UNKNOWN,
    }
}

suspend fun Iterable<CharacterOutput>.toCharacter() = asFlow().map {
    Character(
        charId = it.charId,
        name = it.name,
        birthday = it.birthday,
        occupation = it.occupation,
        img = it.img,
        status = when (it.status) {
            CharacterOutput.Status.ALIVE -> Character.Status.ALIVE
            CharacterOutput.Status.DECEASED -> Character.Status.DECEASED
            CharacterOutput.Status.PRESUME_DEAD -> Character.Status.PRESUME_DEAD
            CharacterOutput.Status.UNKNOWN -> Character.Status.UNKNOWN
        },
        appearance = it.appearance,
        nickname = it.nickname,
        portrayed = it.portrayed
    )
}.toSet()