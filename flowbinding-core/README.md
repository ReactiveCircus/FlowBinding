# FlowBinding Core

This module provides bindings for the **AndroidX Core** library.

## Transitive Dependency

`androidx.core:core`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-core:${flowbinding_version}"
```

## Available Bindings

```kotlin
fun NestedScrollView.scrollChangeEvents(): Flow<ScrollChangeEvent>
```
