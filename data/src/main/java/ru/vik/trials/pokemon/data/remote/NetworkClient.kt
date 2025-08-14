package ru.vik.trials.pokemon.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vik.trials.pokemon.data.BuildConfig
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Клиент для работы с сетью.
 *
 * Позаимствовано с Хабра.
 * */
@Singleton
internal class NetworkClient @Inject constructor() {
    companion object {
        /** Таймаут на операции с сетью, сек. */
        const val TIMEOUT = 30L
    }

    val provideRetrofit = buildRetrofit()

    inline fun <reified T> provideApiService(): T = provideRetrofit.create(T::class.java)

    internal fun buildRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(getHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getHttpClient(): OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()
}