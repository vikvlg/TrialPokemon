package ru.vik.trials.pokemon.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.pokemon.data.BuildConfig
import ru.vik.trials.pokemon.data.local.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppDatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                BuildConfig.DB_NAME)
            .build()
}