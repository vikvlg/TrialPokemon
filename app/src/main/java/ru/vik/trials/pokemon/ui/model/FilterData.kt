package ru.vik.trials.pokemon.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/** Фильтр для списка покемонов. */
@Parcelize
data class FilterData(
    /** Список типов покемонов с флагом активности фильтра. */
    var types: MutableMap<PokemonType, Boolean> = PokemonType.entries.associateBy({ it } , { false } ).toMutableMap(),
): Parcelable