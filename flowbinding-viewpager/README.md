# FlowBinding ViewPager

This module provides bindings for the **AndroidX ViewPager** library.

## Transitive Dependency

`androidx.viewpager:viewpager`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-viewpager:${flowbinding_version}"
```

## Available Bindings

```kotlin
fun ViewPager.pageScrollEvents(): Flow<ViewPagerPageScrollEvent>
fun ViewPager.pageScrollStateChanges(): Flow<Int>
fun ViewPager.pageSelections(emitImmediately: Boolean = false): Flow<Int>
```
