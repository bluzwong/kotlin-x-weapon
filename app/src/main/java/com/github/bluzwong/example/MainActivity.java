package com.github.bluzwong.example;

import android.graphics.Color;
import com.github.bluzwong.kotlin_x_weapon.Kotlin_x_weaponPackage;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.github.bluzwong.example.R;
import com.github.bluzwong.kotlin_x_weapon.SwipeBackActivityHelper;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // 1.get helper
    SwipeBackActivityHelper helper = new SwipeBackActivityHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.background).setBackgroundColor(Color.parseColor(getRandColorCode()));

        // 2.helper init
        helper.initSwipeBack();
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kotlin_x_weaponPackage.startActivity(MainActivity.this, MainActivity.this.getClass());
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        // 3.show animation when back
        helper.onSwipeFinish();
        // last of all set activity theme see AndroidManifest -> android:theme="@style/BluzWong.SwipeBack.Transparent.Theme"
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
