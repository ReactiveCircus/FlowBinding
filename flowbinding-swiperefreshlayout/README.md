# FlowBinding SwipeRefreshLayout

This module provides bindings for the **AndroidX SwipeRefreshLayout** library.

## Transitive Dependency

`androidx.swiperefreshlayout:swiperefreshlayout`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-swiperefreshlayout:${flowbinding_version}"
```

## Available Bindings

```kotlin
fun SwipeRefreshLayout.refreshes(): Flow<Unit>
```
