package ru.vik.trials.pokemon.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.pokemon.data.PokemonRepositoryImpl
import ru.vik.trials.pokemon.domain.PokemonRepository

@Module
@InstallIn(SingletonComponent::class)
interface PokemonRepositoryModule {
    @Binds
    fun bindPokemonRepository(impl: PokemonRepositoryImpl): PokemonRepository
}