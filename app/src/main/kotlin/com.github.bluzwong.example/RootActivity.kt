package com.github.bluzwong.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.bluzwong.kotlin_x_weapon.startActivityEx

/**
 * Created by wangzhijie@wind-mobi.com on 2015/9/16.
 */
public class RootActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_root)
        findViewById(R.id.btn).setOnClickListener {
            startActivityEx(javaClass<MainActivityKt>())
        }
    }
}