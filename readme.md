# Kotlin X Weapon
 Kotlin x weapon is a library of extension functions for android. What is [Kotlin](https://github.com/JetBrains/kotlin)?
 ![1](./screenshot.jpg)
 Example
-----------

####Swipe back finish activity
Kotlin:
```kotlin
                                                    // ↓↓↓↓↓↓ 1.implements this interface
public class MainActivityKt : AppCompatActivity(), SwipeBackActivitySupport {

    // 2.provide activity
    override fun provideActivity(): Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 3.helper init
        initSwipeBack()
        // your other code...
    }

    override fun finish() {
        super<AppCompatActivity>.finish()
        // 4.show animation when back
        onSwipeFinish()
        // 5.set activity theme see AndroidManifest -> android:theme="@style/BluzWong.SwipeBack.Transparent.Theme"
    }
}
```


Java:
```java

public class MainActivity extends AppCompatActivity {
    // 1.get helper
    SwipeBackActivityHelper helper = new SwipeBackActivityHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2.helper init
        helper.initSwipeBack();
        // your other code
    }

    @Override
    public void finish() {
        super.finish();
        // 3.show animation when back
        helper.onSwipeFinish();
        // 4.set activity theme see AndroidManifest -> android:theme="@style/BluzWong.SwipeBack.Transparent.Theme"
    }
}
```

######For best appearance, use transparent theme
AndroidManifest.xml:
```xml
<activity android:name=".MainActivityKt"
          android:theme="@style/BluzWong.SwipeBack.Transparent.Theme"
        >
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```
######Works with ViewPager or others

Kotlin:
```kotlin
// your code ... in onCreate()
val vp = findViewById(R.id.vp) as ViewPager
vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
        //throw UnsupportedOperationException()
        // your code
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //throw UnsupportedOperationException()
        // your code
    }
    // 6. when some views conflict with swipe back , you should do these, for example:
    override fun onPageSelected(position: Int) {
        if (position != 0) {
            // if the current view pager is not the first, make 'vp' receive touch event. so : addTouchOn(vp);
            addTouchOn(vp)
        } else {
            // the current return to the first one, make 'swipe back' receive touch event. so: removeTouchOn(vp);
            // also can helper.removeAllTouchOn();
            removeTouchOn(vp)
        }
        // your code
    }
})
```

Java:
```java
// your code ... in onCreate()
vp = (ViewPager) findViewById(R.id.vp);
vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        if (position != 0) {
            // if the current view pager is not the first, make 'vp' receive touch event. so : helper.addTouchOn(vp);
            helper.addTouchOn(vp);
        } else {
            // the current return to the first one, make 'swipe back' receive touch event. so: helper.removeTouchOn(vp);
            // also can helper.removeAllTouchOn();
            helper.removeTouchOn(vp);
        }
    }
    
    @Override
    public void onPageScrollStateChanged(int state) {}
```
 Dependence
-----------
```groovy
dependencies {
    compile 'com.github.bluzwong:kotlin-x-weapon:0.9.1@aar'
}
```
######to be continued...