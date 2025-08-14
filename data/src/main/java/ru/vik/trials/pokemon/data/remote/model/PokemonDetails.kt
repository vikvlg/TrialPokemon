package ru.vik.trials.pokemon.data.remote.model

import com.google.gson.annotations.SerializedName

internal class PokemonDetails(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<SlotType>,
) {

    class Sprites(
        @SerializedName("back_default")
        val backDefault: String?,
        @SerializedName("front_default")
        val frontDefault: String?
    )

    class Stat(
        @SerializedName("base_stat")
        val value: Int,
        val effort: Int,
        val stat: StatType
    )

    class StatType(
        val name: String,
        val url: String
    )

    class SlotType(
        val slot: Int,
        val type: Type
    )

    class Type(
        val name: String,
        val url: String
    )
}