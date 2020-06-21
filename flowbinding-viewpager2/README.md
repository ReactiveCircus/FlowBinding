# FlowBinding ViewPager2

This module provides bindings for the **AndroidX ViewPager2** library.

## Transitive Dependency

`androidx.viewpager2:viewpager2`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-viewpager2:${flowbinding_version}"
```

## Available Bindings

```kotlin
fun ViewPager2.pageScrollEvents(): Flow<ViewPager2PageScrollEvent>
fun ViewPager2.pageScrollStateChanges(): Flow<Int>
fun ViewPager2.pageSelections(): InitialValueFlow<Int>
```
