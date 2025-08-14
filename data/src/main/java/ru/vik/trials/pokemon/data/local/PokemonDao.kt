package ru.vik.trials.pokemon.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vik.trials.pokemon.data.local.model.PokemonBase
import ru.vik.trials.pokemon.data.local.model.PokemonDetails
import ru.vik.trials.pokemon.data.local.model.PokemonTuple

@Dao
internal interface PokemonDao {
    @Insert(entity = PokemonBase::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(pokemon: PokemonBase)

//    @Query("Insert Into pokemon (name, url) Values (:name, :url)")
//    fun insert(name: String, url: String)

    @Insert(entity = PokemonDetails::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(pokemon: PokemonDetails)

    @Insert(entity = PokemonBase::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<PokemonBase>)

    @Query("Select * From pokemon Where id = :id")
    fun get(id: Int): PokemonTuple?

//    @Query("Select * From pokemon Where name = :name")
//    fun get(name: String): PokemonBase?

//    @Query("SELECT * FROM pokemon\n"
//            + "ORDER BY id, name\n")
//    fun getList(): PagingSource<Int, PokemonBase>

    @Query("SELECT * FROM pokemon\n"
            + "ORDER BY id, name\n")
    fun getList(): PagingSource<Int, PokemonTuple>

    @Query("SELECT count(*) FROM pokemon\n"
            + "ORDER BY id, name\n")
    fun getCount(): Int
}