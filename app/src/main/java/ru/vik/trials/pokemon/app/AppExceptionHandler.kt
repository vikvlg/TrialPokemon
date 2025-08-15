package ru.vik.trials.pokemon.app

import android.util.Log
import kotlin.system.exitProcess

/** Обработчик неотловленных исключений. */
class AppExceptionHandler
    : Thread.UncaughtExceptionHandler {

    companion object {
        private const val TAG = "AppExceptionHandler"
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        Log.e(TAG, "uncaughtException: ${ex.message}", ex)
        //showToastInThread("OOPS!")
        exitProcess(-1)
        //android.os.Process.killProcess(android.os.Process.myPid())
    }
}