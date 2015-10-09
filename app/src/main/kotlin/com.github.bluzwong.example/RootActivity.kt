package com.github.bluzwong.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.bluzwong.example.recyclerview.XRecyclerViewActivity
import com.github.bluzwong.kotlin_x_weapon
import com.github.bluzwong.kotlin_x_weapon.startActivityEx

/**
 * Created by wangzhijie@wind-mobi.com on 2015/9/16.
 */
public class RootActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_root)
        findViewById(R.id.btn).setOnClickListener {
            startActivityEx(javaClass<MainActivity>())
        }
        findViewById(R.id.btn2).setOnClickListener {
            startActivityEx(javaClass<XRecyclerViewActivity>())
//            overridePendingTransition(kotlin_x_weapon.R.anim.slide_in_right, kotlin_x_weapon.R.anim.keep)
        }
    }
}