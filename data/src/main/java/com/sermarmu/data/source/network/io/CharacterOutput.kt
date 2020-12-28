package com.sermarmu.data.source.network.io

import com.google.gson.annotations.SerializedName
import java.util.*

data class CharacterOutput(
    @SerializedName("char_id")
    val charId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("birthday")
    val birthday: Date,
    @SerializedName("occupation")
    val occupation: List<String>,
    @SerializedName("img")
    val img: String,
    @SerializedName("status")
    val status: Status,
    @SerializedName("appearance")
    val appearance: List<Int>?,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("portrayed")
    val portrayed: String
) {
    enum class Status {
        @SerializedName("Alive")
        ALIVE,
        @SerializedName("Deceased")
        DECEASED,
        @SerializedName("Presume dead")
        PRESUME_DEAD,
        @SerializedName("Unknown")
        UNKNOWN,
    }
}