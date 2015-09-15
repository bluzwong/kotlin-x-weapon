package com.github.bluzwong.example

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import com.github.bluzwong.kotlin_x_weapon.SwipeBackActivityHelper
import com.github.bluzwong.kotlin_x_weapon.SwipeBackActivitySupport
import com.github.bluzwong.kotlin_x_weapon.startActivityEx
import java.util.*

/**
 * Created by Bruce-Home on 2015/9/15.
 */
// ↓↓↓↓↓↓ 1.implements this interface
public class MainActivityKt : AppCompatActivity(), SwipeBackActivitySupport {
    override fun provideActivity(): Activity {
        return this
    }

    // 2.provide activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.background).setBackgroundColor(Color.parseColor(MainActivity.getRandColorCode()))
        findViewById(R.id.btn) setOnClickListener {
            startActivityEx(javaClass<MainActivityKt>())
        }

        val views: MutableList<View> = ArrayList<View>()

        val inflater = LayoutInflater.from(this)
        listOf(R.layout.layout1, R.layout.layout2, R.layout.layout3) forEach {
            views add inflater.inflate(it, null)
        }

        val adapter = VPAdapter(views)

        val vp = findViewById(R.id.vp) as ViewPager
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                //throw UnsupportedOperationException()
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //throw UnsupportedOperationException()
                if (position != 0) {
                    addTouchOn(vp)
                } else {
                    removeTouchOn(vp)
                }
            }

            override fun onPageSelected(position: Int) {
                //throw UnsupportedOperationException()
            }

        })
        vp.setAdapter(adapter)
        // 3.helper init
        initSwipeBack()
    }

    override fun finish() {
        super<AppCompatActivity>.finish()
        // 4.show animation when back
        onSwipeFinish()
        // last of all set activity theme see AndroidManifest -> android:theme="@style/BluzWong.SwipeBack.Transparent.Theme"
    }

    override fun onDestroy() {
        super<AppCompatActivity>.onDestroy()
//        removeAllTouchOn()
    }
}