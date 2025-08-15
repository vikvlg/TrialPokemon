package ru.vik.trials.pokemon.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.pokemon.data.remote.NetworkClient
import ru.vik.trials.pokemon.data.remote.PokemonApi

@Module
@InstallIn(SingletonComponent::class)
internal class PokemonApiModule {
    @Provides
    fun providePokemonApi(client: NetworkClient): PokemonApi = client.provideApiService()
}