# FlowBinding Lifecycle

This module provides bindings for the **AndroidX Lifecycle** library.

## Transitive Dependency

`androidx.lifecycle:lifecycle-common-java8`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-lifecycle:${flowbinding_version}"
```

## Available Bindings

```kotlin
fun Lifecycle.events(): Flow<Lifecycle.Event>
```
