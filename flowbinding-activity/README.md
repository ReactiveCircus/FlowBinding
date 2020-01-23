# FlowBinding Activity

This module provides bindings for the **AndroidX Activity** library.

## Transitive Dependency

`androidx.activity:activity-ktx`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-activity:${flowbinding_version}"
```

## Available Bindings

```kotlin
fun OnBackPressedDispatcher.backPresses(enabled: Boolean): Flow<Unit>
```
