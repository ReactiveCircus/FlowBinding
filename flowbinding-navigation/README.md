# FlowBinding Navigation

This module provides bindings for the **AndroidX Navigation** library.

## Transitive Dependency

`androidx.navigation:navigation-runtime`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-navigation:${flowbinding_version}"
```

## Available Bindings

```kotlin
fun NavController.destinationChangeEvents(): Flow<DestinationChangeEvent>
```
