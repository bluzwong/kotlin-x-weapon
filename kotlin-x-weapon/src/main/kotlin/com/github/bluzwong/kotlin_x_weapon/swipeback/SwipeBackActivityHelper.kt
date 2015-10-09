package com.github.bluzwong.kotlin_x_weapon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.support.v4.widget.SlidingPaneLayout
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.github.bluzwong.kotlin_x_weapon.R
import com.github.bluzwong.kotlin_x_weapon.swipeback.SlideView
import com.github.bluzwong.kotlin_x_weapon.swipeback.SwipeLeftView
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Created by Bruce-Home on 2015/9/15.
 */
public class SwipeBackActivityHelper(val activity: Activity) {

    public fun onStartNewActivity(activityCls:Class<*>) {
//        activity.overridePendingTransition(0, R.anim.slide_out_right)
        val activity = activity
        saveView(activity, activity.getWindow().getDecorView())
        val intent = Intent(activity, activityCls)
        intent.putExtra("^^hash$$", activity.hashCode())
//        startActivity(intent)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.keep)
//        activity.overridePendingTransition(R.anim.slide_in_from_bottom,R.anim.slide_out_right);
    }

    public  fun initSwipeBack() {
        var fileName = ""
        val hash = activity.getIntent().getIntExtra("^^hash$$", 0)
        if (hash != 0) {
            fileName = getFileName(activity, hash)
        }
        val slidingPaneLayout = SlideView(activity)
        val field_overHandSize = javaClass<SlidingPaneLayout>().getDeclaredField("mOverhangSize")
        field_overHandSize setAccessible true
        field_overHandSize.set(slidingPaneLayout, 0)
        //        var leftView = LeftView(activity)
        var leftView = SwipeLeftView(activity)
        //        leftView setBackgroundColor Color.WHITE
        leftView setLayoutParams ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        slidingPaneLayout setPanelSlideListener object : SlidingPaneLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                if (!fileName.equals("") && leftView.getTag() == null) {
                    //                    fileName.i("img")
                    val file = File(fileName)
                    if (file.exists()) {
                        val bitMap = BitmapFactory.decodeFile(fileName)
                        leftView.imgView.setImageBitmap(bitMap)
                        leftView.setTag(1)
                        //                        "bitmap create".i(swipeCount)
                    }
                }
                leftView.shadowView.setX(panel!!.getX() - leftView.getWidth())
            }

            override fun onPanelClosed(panel: View?) {
                leftView.imgView.setImageBitmap(null)
                leftView.setTag(null)
                //                "bitmap released ".i(swipeCount)
            }

            override fun onPanelOpened(panel: View?) {
                activity.finish()
                activity.overridePendingTransition(0, R.anim.slide_out_right)
            }
        }
        slidingPaneLayout setSliderFadeColor activity.getResources().getColor(android.R.color.transparent)

        slidingPaneLayout.addView(leftView, 0)
        var decor = activity.getWindow().getDecorView() as ViewGroup

        var decorChild = (decor getChildAt 0) as LinearLayout

        // get origin activity view see http://blog.csdn.net/sunny2come/article/details/8899138
        var contentFrame = decorChild.getChildAt(1) as FrameLayout
        val contentView = contentFrame.getChildAt(0)
        contentView setBackgroundColor activity.getResources().getColor(android.R.color.white)
        // add slidingpanellayout between decor decor view and origin activity view
        slidingPaneLayout.setLayoutParams(contentView.getLayoutParams())
        decorChild removeView contentFrame
        contentFrame removeView contentView
        slidingPaneLayout.addView(contentView, 1)
        decorChild addView slidingPaneLayout
    }

    public fun onSwipeFinish() {
        activity.overridePendingTransition(0, R.anim.slide_out_right_slow);
      //  var decor = activity.getWindow().getDecorView() as ViewGroup
       // var decorChild = (decor getChildAt 0) as LinearLayout
//        val slideView = decorChild.getChildAt(1) as SlideView
        //slideView.openPane() //todo 不知道有什么用
    }

    public fun disableSwipeBack() {
        var decor = activity.getWindow().getDecorView() as ViewGroup
        var decorChild = (decor getChildAt 0) as LinearLayout
        val slideView = decorChild.getChildAt(1) as SlideView
        slideView.disallowIntercept = true
    }

    public fun enableSwipeBack() {
        var decor = activity.getWindow().getDecorView() as ViewGroup
        var decorChild = (decor getChildAt 0) as LinearLayout
        val slideView = decorChild.getChildAt(1) as SlideView
        slideView.disallowIntercept = false
    }
}

public fun saveView(context: Context, decorView: View) {
    val rootView = decorView.getRootView()
    rootView.setDrawingCacheEnabled(true)
    rootView.buildDrawingCache()
    runAsync {
        val bitmap = rootView.getDrawingCache() ?: return@runAsync
        val fileName = getFileName(context,context.hashCode())
        val out = FileOutputStream(fileName)
        val w = bitmap.getWidth()
        val h = bitmap.getHeight()
        var frame = Rect()
        decorView.getWindowVisibleDisplayFrame(frame)
        val statusHeight = frame.top
        Bitmap.createBitmap(bitmap, 0, statusHeight, w, h - statusHeight).compress(Bitmap.CompressFormat.PNG,100, out)
        rootView.setDrawingCacheEnabled(false)
    }
}

public fun getFileName(context: Context, index:Int) :String {
    return  "${context.getApplicationContext().getFilesDir().getAbsolutePath()}/swipeback@${index}.png"
}

