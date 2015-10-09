package com.github.bluzwong.example;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import com.github.bluzwong.example.swipeback.VPAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.github.bluzwong.kotlin_x_weapon.swipeback.SwipebackPackage;
import com.github.bluzwong.kotlin_x_weapon.swipeback.SwipeBackActivityHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // 1.get helper
    SwipeBackActivityHelper helper = new SwipeBackActivityHelper(this);
    ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 2.helper init
        helper.initSwipeBack();
        initViews();
    }

    private void initViews() {
        findViewById(R.id.background).setBackgroundColor(Color.parseColor(getRandColorCode()));
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Kotlin_x_weaponPackage.startActivity(MainActivity.this, MainActivity.this.getClass());
                SwipebackPackage.startSwipeActivity(MainActivity.this, MainActivity.class);
            }
        });

        List<View> views = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(this);
        views.add(inflater.inflate(R.layout.layout1, null));
        views.add(inflater.inflate(R.layout.layout2, null));
        views.add(inflater.inflate(R.layout.layout3, null));
        // your view pager adapter
        VPAdapter adapter = new VPAdapter(views);

        vp = (ViewPager) findViewById(R.id.vp);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 5. when some views conflict with swipe back , you should do these, for example:
                if (position != 0) {
                    // if the current view page is not the first, make 'vp' receive touch event.
                    helper.disableSwipeBack();
                } else {
                    // the current page return to the first one, make 'swipe back' receive touch event.
                    helper.enableSwipeBack();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        vp.setAdapter(adapter);
    }

    @Override
    public void finish() {
        super.finish();
        // 3.show animation when back
        helper.afterSwipeFinish();
        // 4.set activity theme see AndroidManifest -> android:theme="@style/BluzWong.SwipeBack.Transparent.Theme"
    }

    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        return "#" + r + g + b;
    }
}
