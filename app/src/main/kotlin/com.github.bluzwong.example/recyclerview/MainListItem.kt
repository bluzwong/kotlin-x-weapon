package com.github.bluzwong.example.recyclerview

import java.io.Serializable

/**
 * Created by Bruce-Home on 2015/9/5.
 */
public data class MainListItem(val itemResId: Int, val lineName: String,
                               val distance1: String = "", val stop1: String = ""/*, val stop1Direction: String = ""*/,
                               val distance2: String = "", val stop2: String = ""/*, val stop2Direction: String = ""*/) : Serializable {
    override fun equals(other: Any?): Boolean {
        return lineName equals (other as MainListItem).lineName
    }
}