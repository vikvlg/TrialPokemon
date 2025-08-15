package ru.vik.trials.pokemon.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppPokemon : Application() {

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler(AppExceptionHandler())

        // DEBUG: Очистка кеша программы.
        //ru.vik.trials.pokemon.data.local.removeAppDatabase(this)
    }
}