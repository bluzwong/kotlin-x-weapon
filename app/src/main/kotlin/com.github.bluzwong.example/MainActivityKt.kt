package com.github.bluzwong.example

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.bluzwong.kotlin_x_weapon.SwipeBackActivitySupport
import com.github.bluzwong.kotlin_x_weapon.startActivityEx

/**
 * Created by Bruce-Home on 2015/9/15.
 */                                              // ↓↓↓↓↓↓ 1.implements this interface
public class MainActivityKt : AppCompatActivity(), SwipeBackActivitySupport {

    // 2.provide activity
    override fun provideActivity(): Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 3.helper init
        initSwipeBack()

        findViewById(R.id.background).setBackgroundColor(Color.parseColor(MainActivity.getRandColorCode()))
        findViewById(R.id.btn) setOnClickListener {
            startActivityEx(javaClass<MainActivityKt>())
        }
    }

    override fun finish() {
        super<AppCompatActivity>.finish()
        // 4.show animation when back
        onSwipeFinish()
        // last of all set activity theme see AndroidManifest -> android:theme="@style/BluzWong.SwipeBack.Transparent.Theme"
    }
}