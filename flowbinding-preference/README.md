# FlowBinding Preference

This module provides bindings for the **AndroidX Preference** library.

## Transitive Dependency

`androidx.preference:preference`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-preference:${flowbinding_version}"
```

## Available Bindings

```kotlin
// Preference
fun Preference.preferenceChanges(): Flow<Any>
fun Preference.preferenceClicks(): Flow<Unit>

// EditTextPreference
fun EditTextPreference.editTextBindEvents(): Flow<EditTextBindEvent>
```
