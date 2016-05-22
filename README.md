# DotsView
Индикаторы для ViewPager

## Подключение библиотеки (Gradle)

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

buildscript {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    compile 'com.github.e16din:DotsView:0.1.0'
}
```

## Пример использования
```xml
<android.support.v4.view.ViewPager
            android:id="@+id/vPager"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

<com.e16din.dotsview.DotsView
    android:id="@+id/vDots"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:count="4"
    app:padding="10dp"
    app:selector="@drawable/selector_pager_indicator"
    app:size="8dp"
    app:viewPager="@+id/vPager"/>
```

## Пример селектора (selector_pager_indicator)
```xml
<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:drawable="@drawable/circle_selected" android:state_selected="true"/>
    <item android:drawable="@drawable/circle_default" android:state_selected="false"/>
    <item android:drawable="@drawable/circle_default"/>

</selector>
```