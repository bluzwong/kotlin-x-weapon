package com.github.bluzwong.kotlin_x_weapon

import android.util.Log

/**
 * Created by Bruce-Home on 2015/9/2.
 */
public fun <T> T.v(msg: Any = ""): T {
    var info = formatLogInfo()
    Log.v(info[0], "${info[1]}${this.toString()}${info[2]} msg: ${msg}\n${info[3]}")
    return this
}

public fun <T> T.d(msg: Any = ""): T {
    var info = formatLogInfo()
    Log.d(info[0], "${info[1]}${this.toString()}${info[2]} msg: ${msg}\n${info[3]}")
    return this
}

public fun <T> T.i(msg: Any = ""): T {
    var info = formatLogInfo()
    Log.i(info[0], "${info[1]}${this.toString()}${info[2]} msg: ${msg}\n${info[3]}")
    return this
}

public fun <T> T.w(msg: Any = ""): T {
    var info = formatLogInfo()
    Log.w(info[0], "${info[1]}${this.toString()}${info[2]} msg: ${msg}\n${info[3]}")
    return this
}

public fun <T> T.e(msg: Any = ""): T {
    var info = formatLogInfo()
    Log.e(info[0], "${info[1]}${this.toString()}${info[2]} msg: ${msg}\n${info[3]}")
    return this
}

public fun <T> T.wtf(msg: Any = ""): T {
    var info = formatLogInfo()
    Log.wtf(info[0], "${info[1]}${this.toString()}${info[2]} msg: ${msg}\n${info[3]}")
    return this
}


public fun formatLogInfo(): Array<String> {
    var st = Thread.currentThread().getStackTrace()[5]
    var fileName = st.getFileName()
    var line = st.getLineNumber()
    var className = st.getClassName()
    var methodName = st.getMethodName()
    var simpleClassName = className.subSequence((className lastIndexOf '.') + 1, className.lastIndex + 1).toString()
    return arrayOf(simpleClassName, "${methodName}(): -> ", " <- ", " at ${className}(${fileName}:${line})")
}