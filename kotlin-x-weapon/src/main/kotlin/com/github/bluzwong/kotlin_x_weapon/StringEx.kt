package com.github.bluzwong.kotlin_x_weapon

import android.content.Context
import android.widget.Toast

/**
 * Created by Bruce-Home on 2015/9/2.
 */
var toastInstance: Toast? = null
var toastInstanceLong: Toast? = null
public fun String.toast(context: Context): String {
    if (toastInstance == null) {
        toastInstance = Toast.makeText(context, this, Toast.LENGTH_SHORT)
    }
    toastInstance?.show()
    return this
}

public fun String.toastLong(context: Context): String {
    if (toastInstanceLong == null) {
        toastInstanceLong = Toast.makeText(context, this, Toast.LENGTH_LONG)
    }
    toastInstanceLong?.show()
    return this
}


