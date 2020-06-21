# FlowBinding RecyclerView

This module provides bindings for the **AndroidX RecyclerView** library.

## Transitive Dependency

`androidx.recyclerview:recyclerview`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-recyclerview:${flowbinding_version}"
```

## Available Bindings

```kotlin
fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> T.dataChanges(): InitialValueFlow<T>
fun RecyclerView.childAttachStateChangeEvents(): Flow<RecyclerViewChildAttachStateChangeEvent>
fun RecyclerView.flingEvents(handled: (FlingEvent) -> Boolean = { true }): Flow<FlingEvent>
fun RecyclerView.scrollEvents(): Flow<RecyclerViewScrollEvent>
fun RecyclerView.scrollStateChanges(): Flow<Int>
```
