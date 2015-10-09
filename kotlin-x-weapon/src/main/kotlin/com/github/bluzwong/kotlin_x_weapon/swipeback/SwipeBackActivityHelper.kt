package com.github.bluzwong.kotlin_x_weapon.swipeback

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.support.v4.widget.SlidingPaneLayout
import android.util.LruCache
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.github.bluzwong.kotlin_x_weapon.R
import com.github.bluzwong.kotlin_x_weapon.runAsync
import java.io.File
import java.io.FileOutputStream
import java.lang.Deprecated
import java.util.*

/**
 * Created by Bruce-Home on 2015/9/15.
 */
public class SwipeBackActivityHelper(val activity: Activity) {
    private var fileName = ""
    public  fun initSwipeBack() {
        val hash = activity.intent.getIntExtra("^^hash$$", 0)
        if (hash != 0) {
            fileName = getFileName(activity, hash)
        }
        val slidingPaneLayout = SlideView(activity)
        val field_overHandSize = SlidingPaneLayout::class.java.getDeclaredField("mOverhangSize")
        field_overHandSize setAccessible true
        field_overHandSize.set(slidingPaneLayout, 0)
        var leftView = SwipeLeftView(activity)
        leftView setLayoutParams ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        slidingPaneLayout setPanelSlideListener object : SlidingPaneLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                if (!fileName.isNullOrEmpty() && leftView.tag == null) {
                    //                    fileName.i("img")
                    var bitMap = cachedScreenShot.get(fileName)
                    if (bitMap == null) {
                        val file = File(fileName)
                        if (file.exists()) {
                            bitMap = BitmapFactory.decodeFile(fileName)
                            cachedScreenShot.put(fileName, bitMap)
                            //                        "bitmap create".i(swipeCount)
                        }
                    }
                    if (bitMap != null) {
                        leftView.imgView.setImageBitmap(bitMap)
                        leftView.tag = 1
                    }

                }
                leftView.shadowView.x = panel!!.x - leftView.width
            }

            override fun onPanelClosed(panel: View?) {
                leftView.imgView.setImageBitmap(null)
                leftView.tag = null
                //                "bitmap released ".i(swipeCount)
            }

            override fun onPanelOpened(panel: View?) {
                cleanScreenShot(fileName, activity.applicationContext)
                activity.finish()
                activity.overridePendingTransition(0, R.anim.slide_out_right)
            }
        }
        slidingPaneLayout setSliderFadeColor activity.resources.getColor(android.R.color.transparent)

        slidingPaneLayout.addView(leftView, 0)
        var decor = activity.window.decorView as ViewGroup

        var decorChild = (decor getChildAt 0) as LinearLayout

        // get origin activity view see http://blog.csdn.net/sunny2come/article/details/8899138
        var contentFrame = decorChild.getChildAt(1) as FrameLayout
        val contentView = contentFrame.getChildAt(0)
        contentView setBackgroundColor activity.resources.getColor(android.R.color.white)
        // add slidingpanellayout between decor decor view and origin activity view
        slidingPaneLayout.layoutParams = contentView.layoutParams
        decorChild removeView contentFrame
        contentFrame removeView contentView
        slidingPaneLayout.addView(contentView, 1)
        decorChild addView slidingPaneLayout
    }

    @Deprecated
    public fun onSwipeFinish() {
        afterSwipeFinish()
    }

    public fun afterSwipeFinish() {
        cleanScreenShot(fileName, activity.applicationContext)
        activity.overridePendingTransition(0, R.anim.slide_out_right_slow);
        //  var decor = activity.getWindow().getDecorView() as ViewGroup
        // var decorChild = (decor getChildAt 0) as LinearLayout
        //        val slideView = decorChild.getChildAt(1) as SlideView
        //slideView.openPane() //todo 不知道有什么用
    }

    public fun disableSwipeBack() {
        var decor = activity.window.decorView as ViewGroup
        var decorChild = (decor getChildAt 0) as LinearLayout
        val slideView = decorChild.getChildAt(1) as SlideView
        slideView.disallowIntercept = true
    }

    public fun enableSwipeBack() {
        var decor = activity.window.decorView as ViewGroup
        var decorChild = (decor getChildAt 0) as LinearLayout
        val slideView = decorChild.getChildAt(1) as SlideView
        slideView.disallowIntercept = false
    }
}

public fun startSwipeActivity(activity: Activity, activityCls:Class<*>) {
    //        activity.overridePendingTransition(0, R.anim.slide_out_right)
    startSwipeActivity(activity, Intent(activity, activityCls))
}

public fun startSwipeActivity(activity: Activity, intent:Intent) {
    //        activity.overridePendingTransition(0, R.anim.slide_out_right)
    saveView(activity, activity.window.decorView)
    intent.putExtra("^^hash$$", activity.hashCode())
    //        startActivity(intent)
    activity.startActivity(intent)
    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.keep)
    //        activity.overridePendingTransition(R.anim.slide_in_from_bottom,R.anim.slide_out_right);
}

private val cachedScreenShot = object :LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory()/8L).toInt()) {
    override fun sizeOf(key: String?, value: Bitmap?): Int {
        return value!!.rowBytes * value.height
    }
}

private val activityHashList = ArrayList<String>()

private fun cleanScreenShot(fileName:String, context: Context) {
    removeUnusedFiles(context)
    if (fileName.isNullOrEmpty()) return
    cachedScreenShot.remove(fileName)
    val screenShotFile = File(fileName)
    if (screenShotFile.exists()) {
        runAsync {
            screenShotFile.delete()
        }
    }
}
private fun removeUnusedFiles(context: Context) {
    val dir = context.applicationContext.filesDir.absolutePath
    val files = File(dir).listFiles()
    for (file in files) {
        var isUsed = false
        for (hash in activityHashList) {
            if (hash in file.name) {
                isUsed = true
                break
            }
        }
        if (!isUsed) {
            file.delete()
        }
    }
}

private fun saveView(context: Context, decorView: View) {
    val rootView = decorView.rootView
    rootView.isDrawingCacheEnabled = true
    rootView.buildDrawingCache()
    runAsync {
        val bitmap = rootView.drawingCache ?: return@runAsync
        val hashCode = context.hashCode()
        val fileName = getFileName(context,hashCode)
        val out = FileOutputStream(fileName)
        val w = bitmap.width
        val h = bitmap.height
        var frame = Rect()
        decorView.getWindowVisibleDisplayFrame(frame)
        val statusHeight = frame.top
        val finalBitmap = Bitmap.createBitmap(bitmap, 0, statusHeight, w, h - statusHeight)
        cachedScreenShot.put(fileName, finalBitmap)
        activityHashList.add(hashCode.toString())
        finalBitmap.compress(Bitmap.CompressFormat.PNG,100, out)
        rootView.isDrawingCacheEnabled = false
    }
}

private fun getFileName(context: Context, index:Int) :String {
    return  "${context.applicationContext.filesDir.absolutePath}/swipeback@$index.png"
}

