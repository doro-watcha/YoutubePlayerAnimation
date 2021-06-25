package com.goddoro.youtubeplayer.utils

import android.util.Log
import com.goddoro.youtubeplayer.BuildConfig


fun debugE(tag: String, message: Any?) {
    if (BuildConfig.DEBUG)
        Log.e(tag, "🧩" + message.toString() + "🧩")
}

fun debugE(message: Any?) {
    debugE("DEBUG", message)
}