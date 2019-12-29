# FlowBinding

[![CircleCI](https://circleci.com/gh/ReactiveCircus/FlowBinding.svg?style=svg)](https://circleci.com/gh/ReactiveCircus/FlowBinding)
[![Build Status](https://api.cirrus-ci.com/github/ReactiveCircus/FlowBinding.svg)](https://cirrus-ci.com/github/ReactiveCircus/FlowBinding)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.reactivecircus.flowbinding/flowbinding-android/badge.svg)](https://search.maven.org/search?q=g:io.github.reactivecircus.flowbinding)
[![Android API](https://img.shields.io/badge/API-21%2B-blue.svg?label=API&maxAge=300)](https://www.android.com/history/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Kotlin Flow binding APIs for Android's platform and unbundled UI widgets, inspired by [RxBinding][rxbinding].

[Flow][flow] is (conceptually) a reactive streams implementation provided by the [kotlinx-coroutines-core][kotlinx-coroutines] artifact.

**FlowBinding** offers an extensive set of extension functions that turn traditional callbacks / listeners on Android UI widgets into the `Flow` type.

## Article

Blog post published in [Kotlin Weekly #170][kotlin-weekly] - [Binding Android UI with Kotlin Flow][article]

## Download

Dependencies are hosted on [Maven Central][maven-central].

Latest version:

```groovy
def flowbinding_version = "0.7.0"
```

### Platform Bindings

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-android:${flowbinding_version}"
```

### AndroidX Bindings

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-appcompat:${flowbinding_version}"
implementation "io.github.reactivecircus.flowbinding:flowbinding-core:${flowbinding_version}"
implementation "io.github.reactivecircus.flowbinding:flowbinding-drawerlayout:${flowbinding_version}"
implementation "io.github.reactivecircus.flowbinding:flowbinding-navigation:${flowbinding_version}"
implementation "io.github.reactivecircus.flowbinding:flowbinding-preference:${flowbinding_version}"
implementation "io.github.reactivecircus.flowbinding:flowbinding-recyclerview:${flowbinding_version}"
implementation "io.github.reactivecircus.flowbinding:flowbinding-swiperefreshlayout:${flowbinding_version}"
implementation "io.github.reactivecircus.flowbinding:flowbinding-viewpager2:${flowbinding_version}"
```

### Material Components Bindings

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-material:${flowbinding_version}"
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

## Usage

### Binding UI Events 

To observe click events on an Android `View`:

```kotlin
findViewById<Button>(R.id.button)
    .clicks() // binding API available in flowbinding-android
    .onEach {
        // handle button clicked
    }
    .launchIn(uiScope)
```

### Binding Scope

`launchIn(scope)` is a shorthand for `scope.launch { flow.collect() }` provided by the **kotlinx-coroutines-core** library.

This `uiScope` in the example above is a `CoroutineScope` that defines the lifecycle of this `Flow`. The binding implementation will respect this scope by unregistering the callback / listener automatically when the scope is cancelled.

In the context of Android lifecycle this means the `uiScope` passed in here should be a scope that's bound to the `Lifecycle` of the view the widget lives in.

`androidx.lifecycle:lifecycle-runtime-ktx:2.2.0` introduced an extension property `LifecycleOwner.lifecycleScope: LifecycleCoroutineScope` which will be cancelled when the `Lifecycle` is destroyed.

In an `Activity` it might look something like this:


```kotlin
class ExampleActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        findViewById<ViewPager2>(R.id.viewPager)
            .pageSelections() // binding API available in flowbinding-viewpager2
            .onEach { pagePosition ->
                // handle pagePosition
            }
            .launchIn(lifecycleScope) // provided by lifecycle-runtime-ktx 
            
    }
}
```

Note that with **FlowBinding** you no longer need to unregister / remove listeners or callbacks in `onDestroy()` as this is done automatically for you.

### Binding UI Events with Additional Information

Some UI widgets might hold a state internally which you might want to observe with a `Flow`.

For example with a `TabLayout` you might want to observe and react to the Tab selection events. In this case the binding API returns a `Flow` of custom `TabLayoutSelectionEvent` type which contains the currently selected `Tab`:

```kotlin
tabLayout.tabSelectionEvents()
    .filterIsInstance<TabLayoutSelectionEvent.TabSelected>() // only care about TabSelected events
    .onEach { event ->
        // sync selected tab title to some other UI element
        selectedTabTitle.text = event.tab.text
    }
    .launchIn(uiScope)
``` 

### Eager Mode

In some cases you might want the `Flow` to emit the **current** value immediately when the `Flow` is collected.

For example a **Slider** might have a default `value` which was set in XML, and we want to bind the current value of the Slider to some other UI element as soon as the screen is launched without the value of the slider being changed by user.

The binding APIs for this kind of widgets have an optional `emitImmediately: Boolean` parameter which controls whether to emit the current value immediately on flow collection:

```kotlin
slider.valueChanges(emitImmediately = true)
    .onEach { value ->
        // handle value
    }
    .launchIn(uiScope)
```

### Additional Samples

All binding APIs are documented with **Example of usage**.

All bindings are covered by **instrumented tests** which you may want to refer to.  

## Roadmap

Our goal is to provide most of the bindings provided by **RxBinding**, while shifting our focus to supporting more modern **AndroidX** APIs such as **ViewPager2** and the new components in **Material Components** as they become available.

List of all bindings available:

* [Platform bindings][flowbinding-android]
* [AndroidX AppCompat bindings][flowbinding-appcompat]
* [AndroidX Core bindings][flowbinding-core]
* [AndroidX DrawerLayout bindings][flowbinding-drawerlayout]
* [AndroidX Navigation Component bindings][flowbinding-navigation]
* [AndroidX Preference bindings][flowbinding-preference]
* [AndroidX RecyclerView bindings][flowbinding-recyclerview]
* [AndroidX SwipeRefreshLayout bindings][flowbinding-swiperefreshlayout]
* [AndroidX ViewPager2 bindings][flowbinding-viewpager2]
* [Material Components bindings][flowbinding-material]

Please feel free to create an issue if you think a useful binding is missing or you want a new binding added to the library.

## Credits

This library is inspired by [RxBinding][rxbinding] which provides RxJava binding APIs for Android's UI widgets.

Many thanks to RxBinding's author [Jake Wharton][jake] and its contributors.

## License

```
Copyright 2019 Yang Chen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
[kotlin-weekly]: https://mailchi.mp/kotlinweekly/kotlin-weekly-170
[article]: https://dev.to/ychescale9/binding-android-ui-with-kotlin-flow-22ok
[maven-central]: https://search.maven.org/search?q=g:io.github.reactivecircus.flowbinding
[snap]: https://oss.sonatype.org/content/repositories/snapshots/
[rxbinding]: https://github.com/JakeWharton/RxBinding
[jake]: https://github.com/JakeWharton
[flow]: https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/
[kotlinx-coroutines]: https://github.com/Kotlin/kotlinx.coroutines
[flowbinding-android]: flowbinding-android/
[flowbinding-appcompat]: flowbinding-appcompat/
[flowbinding-core]: flowbinding-core/
[flowbinding-drawerlayout]: flowbinding-drawerlayout/
[flowbinding-material]: flowbinding-material/
[flowbinding-navigation]: flowbinding-navigation/
[flowbinding-preference]: flowbinding-preference/
[flowbinding-recyclerview]: flowbinding-recyclerview/
[flowbinding-swiperefreshlayout]: flowbinding-swiperefreshlayout/
[flowbinding-viewpager2]: flowbinding-viewpager2/
