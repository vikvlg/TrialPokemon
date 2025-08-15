package ru.vik.trials.pokemon.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vik.trials.pokemon.data.local.model.PokemonBase
import ru.vik.trials.pokemon.data.local.model.PokemonDetails
import ru.vik.trials.pokemon.data.local.model.PokemonTuple

/** DAO для работы с покемонами в БД. */
@Dao
internal interface PokemonDao {
    /** Добавляет базовую информацию по покемону. */
    @Insert(entity = PokemonBase::class, onConflict = OnConflictStrategy.IGNORE)
    fun insert(pokemon: PokemonBase)

    /** Добавляет дополнительную информацию по покемону. */
    @Insert(entity = PokemonDetails::class, onConflict = OnConflictStrategy.IGNORE)
    fun insert(pokemon: PokemonDetails)

    /** Добавляет список с базовой информацией по покемонам. */
    @Insert(entity = PokemonBase::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(list: List<PokemonBase>)

    /** Возвращает объединенную информацию по покемону. */
    @Query("Select * From pokemon Where id = :id")
    fun get(id: Int): PokemonTuple?

    /** Возвращает пагинированный список покемонов. */
    @Query("SELECT * FROM pokemon\n"
            + "ORDER BY id, name\n")
    fun getList(): PagingSource<Int, PokemonTuple>

    /** Возвращает количество покемонов в БД. */
    @Query("SELECT count(*) FROM pokemon\n"
            + "ORDER BY id, name\n")
    fun getCount(): Int
}