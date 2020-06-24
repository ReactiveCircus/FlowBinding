# FlowBinding AppCompat

This module provides bindings for the **AndroidX AppCompat** library.

## Transitive Dependency

`androidx.appcompat:appcompat`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-appcompat:${flowbinding_version}"
```

## Available Bindings

```kotlin
// ActionMenuView
fun ActionMenuView.itemClicks(): Flow<MenuItem>

// PopupMenu
fun PopupMenu.dismisses(): Flow<Unit>
fun PopupMenu.itemClicks(): Flow<MenuItem>

// SearchView
fun SearchView.queryTextChanges(): InitialValueFlow<CharSequence>
fun SearchView.queryTextEvents(): InitialValueFlow<QueryTextEvent>

// Toolbar
fun Toolbar.itemClicks(): Flow<MenuItem>
fun Toolbar.navigationClicks(): Flow<Unit>
```
