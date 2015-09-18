package com.github.bluzwong.example.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.github.bluzwong.example.R

/**
 * Created by Bruce-Home on 2015/9/5.
 */
open public class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // listitem_main_activity.xml
    public val tvLineTitle: TextView = itemView.findViewById(R.id.autotv_list_item_line_title) as TextView
    public val llLine: View = itemView.findViewById(R.id.ll_incard_line)
}