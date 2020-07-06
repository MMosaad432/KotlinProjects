package com.example.popularmovies.utils

import androidx.annotation.NonNull
import java.util.concurrent.Executor

class MyExecutor : Executor {
    override fun execute(@NonNull runnable: Runnable) {
        Thread(runnable).start()
    }
}