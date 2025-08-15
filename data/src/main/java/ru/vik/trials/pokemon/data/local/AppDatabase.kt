package ru.vik.trials.pokemon.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vik.trials.pokemon.data.BuildConfig
import ru.vik.trials.pokemon.data.local.model.PokemonBase
import ru.vik.trials.pokemon.data.local.model.PokemonDetails

/** Клиент по работе с БД (кешем) программы. */
@Database(
    version = 1,
    entities = [
        PokemonBase::class,
        PokemonDetails::class,
    ]
)
internal abstract class AppDatabase : RoomDatabase() {
    /** Возвращает объект для работы с покемонами. */
    abstract fun getPokemonDao(): PokemonDao
}

/**
 * Удаляет файл БД.
 *
 * Нужно для отладки приложения, чтобы не заморачиваться с версионностью.
 * */
fun removeAppDatabase(context: Context) {
    val file = context.getDatabasePath(BuildConfig.DB_NAME)
    if (file.exists() && file.isFile) {
        file.delete()
    }
}
