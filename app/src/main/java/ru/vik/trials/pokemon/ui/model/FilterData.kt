package ru.vik.trials.pokemon.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/** Фильтр для списка покемонов. */
@Parcelize
data class FilterData(
    var name: String = "",
    var type: String = "",
    var typeMap: MutableMap<PokemonType, Boolean> = PokemonType.entries.associateBy({ it } , { false } ).toMutableMap(),
): Parcelable {
}