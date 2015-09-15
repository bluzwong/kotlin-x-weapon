package com.github.bluzwong.kotlin_x_weapon

import android.os.Handler
import android.os.Looper

/**
 * Created by Bruce on 15/9/3.
 */
public fun runAsync(action: () -> Unit) {
    Thread(Runnable(action)).start()
}

public fun runOnUiThread(action: () -> Unit) {
    Handler(Looper.getMainLooper()).post(Runnable(action))
}

public fun runDelayed(delayMillis: Long, action: () -> Unit) {
    Handler().postDelayed(Runnable(action), delayMillis)
}

public fun runDelayedOnUiThread(delayMillis: Long, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(Runnable(action), delayMillis)
}