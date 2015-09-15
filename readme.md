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
}

```

