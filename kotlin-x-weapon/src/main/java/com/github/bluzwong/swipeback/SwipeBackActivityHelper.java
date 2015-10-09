package com.github.bluzwong.swipeback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.github.bluzwong.kotlin_x_weapon.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhijie on 2015/10/9.
 */
public class SwipeBackActivityHelper {
    private Activity activity;
    private SwipeBackView swipeBackView;
    private int hashCode = 0;
    private String fileName = "";

    public SwipeBackActivityHelper(Activity activity) {
        this.activity = activity;
    }

    public void init() {
        final int screenShotHashCode = activity.getIntent().getIntExtra("^^hash$$", 0);
        if (screenShotHashCode != 0) {
            hashCode = screenShotHashCode;
            fileName = getFileName(activity, screenShotHashCode);
        }
        swipeBackView = new SwipeBackView(activity);
        try {
            Field field_overHandSize = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
            field_overHandSize.setAccessible(true);
            field_overHandSize.set(swipeBackView, 0);
            final SwipeBackLeftView leftView = new SwipeBackLeftView(activity);
            leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            swipeBackView.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View view, float v) {
                    leftView.shadowView.setX(view.getX() - leftView.getWidth());
                    if (!TextUtils.isEmpty(fileName) && leftView.getTag() == null) {
                        Bitmap bitmap = cachedScreenShot.get(fileName);
                        if (bitmap == null) {
                            File file = new File(fileName);
                            if (file.exists()) {
                                bitmap = BitmapFactory.decodeFile(fileName);
                                cachedScreenShot.put(fileName, bitmap);
                            }
                        }
                        if (bitmap != null) {
                            leftView.imgView.setImageBitmap(bitmap);
                            leftView.setTag(1);
                        }
                    }
                }

                @Override
                public void onPanelOpened(View view) {
                    removeScreenShot(activity, screenShotHashCode);
                    activity.finish();
                    activity.overridePendingTransition(0, R.anim.slide_out_right);
                }

                @Override
                public void onPanelClosed(View view) {
                    leftView.imgView.setImageBitmap(null);
                    leftView.setTag(null);
                }
            });
            swipeBackView.setSliderFadeColor(Color.TRANSPARENT);
            swipeBackView.addView(leftView, 0);
            ViewGroup decorView = getDecorView();
            LinearLayout decorChild = (LinearLayout) decorView.getChildAt(0);
            FrameLayout contentFrame = (FrameLayout) decorChild.getChildAt(1);
            View contentView = contentFrame.getChildAt(0);
            contentView.setBackgroundColor(Color.WHITE);
            swipeBackView.setLayoutParams(contentView.getLayoutParams());
            contentFrame.removeView(contentView);
            swipeBackView.addView(contentView , 1);
            contentFrame.addView(swipeBackView);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public void afterFinish() {
        if (hashCode != 0) {
            removeScreenShot(activity, hashCode);
        }
        activity.overridePendingTransition(0, R.anim.slide_out_right_slow);
    }

    public void disableSwipeBack() {
        swipeBackView.disallowIntercept = true;
    }

    public void enableSwipeBack() {
        swipeBackView.disallowIntercept = false;
    }

    private ViewGroup getDecorView() {
        return (ViewGroup) activity.getWindow().getDecorView();
    }

    public static void startSwipeActivity(Activity activity, Class cls) {
        startSwipeActivity(activity, new Intent(activity, cls));
    }
    public static void startSwipeActivity(Activity activity, Intent intent) {
        saveScreenShot(activity);
        intent.putExtra("^^hash$$", activity.hashCode());
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.keep);
    }

    private static final LruCache<String, Bitmap> cachedScreenShot
            = new LruCache<String, Bitmap>((int)(Runtime.getRuntime().maxMemory()/8)) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }
    };

    private static final List<String> activityHashList = new ArrayList<String>();

    private static void removeScreenShot(Context context, int hashCode) {
        removeUnusedScreenShot(context);
        String fileName = getFileName(context, hashCode);
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        cachedScreenShot.remove(fileName);
        File screenShotFile = new File(fileName);
        if (screenShotFile.exists()) {
            screenShotFile.delete();
        }
    }

    private static void removeUnusedScreenShot(Context context) {
        String cacheDir = getCacheDir(context);
        for (File file : new File(cacheDir).listFiles()) {
            boolean isUsed = false;
            for (String hash : activityHashList) {
                if (file.getName().contains(hash)) {
                    isUsed = true;
                    break;
                }
            }
            if (!isUsed) {
                file.delete();
            }
        }
    }

    private static void saveScreenShot(final Activity activity) {
        final View decorView = activity.getWindow().getDecorView();
        final View rootView = decorView.getRootView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = rootView.getDrawingCache();
                if (bitmap == null) {
                    rootView.setDrawingCacheEnabled(false);
                    return;
                }
                int hashCode = activity.hashCode();
                String fileName = getFileName(activity);
                try {
                    FileOutputStream out = new FileOutputStream(fileName);
                    Rect frame = new Rect();
                    decorView.getWindowVisibleDisplayFrame(frame);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int statusHeight = frame.top;
                    if (statusHeight > 0) {
                        bitmap = Bitmap.createBitmap(bitmap, 0, statusHeight, width, height - statusHeight);
                    }
                    cachedScreenShot.put(fileName, bitmap);
                    activityHashList.add(String.valueOf(hashCode));
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    rootView.setDrawingCacheEnabled(false);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static String getCacheDir(Context context) {
        return context.getApplicationContext().getFilesDir().getAbsolutePath();
    }

    private static String getFileName(Context context) {
        return getFileName(context, context.hashCode());
    }
    private static String getFileName(Context context, int hashCode) {
        return getCacheDir(context) + "/swipeback@@" + hashCode + "$$.png";
    }
}
