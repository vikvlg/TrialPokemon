package ru.vik.trials.pokemon.data.remote.model

import com.google.gson.annotations.SerializedName

/** Детальная информация по покемону. */
internal class PokemonDetails(
    /** Идентификатор. */
    val id: Int,

    /** Имя. */
    val name: String,

    /** Изображение. */
    val sprites: Sprites,

    /** Параметры. */
    val stats: List<Stat>,

    /** Типы. */
    val types: List<SlotType>,
) {

    /** Изображения покемона. */
    class Sprites(
        /** Ссылка на изображение со спины. */
        @SerializedName("back_default")
        val backDefault: String?,

        /** Ссылка на изображение спереди. */
        @SerializedName("front_default")
        val frontDefault: String?
    )

    /** Параметры покемона. */
    class Stat(
        /** Величина параметра. */
        @SerializedName("base_stat")
        val value: Int,

        /** Какие-то Effort points. Наверное фанаты покемонов понимают что это. */
        val effort: Int,

        /** Параметр. */
        val stat: StatType
    )

    /** Тип параметра покемона. */
    class StatType(
        /** Название параметра. */
        val name: String,

        /** Ссылка на получение информации по параметру. */
        val url: String
    )

    /** Тип покемона. */
    class SlotType(
        /** Порядковый номер. */
        val slot: Int,

        /** Тип покемона. */
        val type: Type
    )

    /** Тип покемона. */
    class Type(
        /** Название. */
        val name: String,

        /** Ссылка на получение информации по типу. */
        val url: String
    )
}