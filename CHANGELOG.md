# Change Log

## Version 1.0.0-alpha03

_2020-08-16_

* Update to Kotlin 1.4.0 and Coroutines 1.3.9.
* Enable explicit API mode.
* Update AndroidX and Material Components.

## Version 1.0.0-alpha02

_2020-06-27_

* Fix: Explicitly adding the `flowbinding-common` dependency is no longer required.

## Version 1.0.0-alpha01

_2020-06-26_

* Breaking change: Introduced a new `InitialValueFlow` type for the bindings which emit state. All bindings with the `emitImmediately: Boolean = false` param have been migrated to to return `InitialValueFlow`. This is also a behavior change as the current value of a widget will now be emitted immediately upon collection. The new `InitialValueFlow` provides a `skipInitialValue()` function for skipping the initial emission.
* Dependency updates:
  * `drawerlayout:1.1.0`
  * `fragment:1.2.5`
  * `navigation:2.3.0`
  * `swiperefreshlayout:1.1.0`

## Version 0.12.0

_2020-05-29_

* New: Added new bindings for Material Components:
  * `fun Slider.touchEvents(): Flow<SliderTouchEvent>`
  * `fun RangeSlider.valuesChanges(emitImmediately: Boolean = false): Flow<List<Float>>`
  * `fun RangeSlider.changeEvents(emitImmediately: Boolean = false): Flow<RangeSliderChangeEvent>`
  * `fun RangeSlider.touchEvents(): Flow<RangeSliderTouchEvent>`
  * `fun TextInputLayout.errorIconClicks(): Flow<Unit>`
  * `fun TextInputLayout.errorIconLongClicks(): Flow<Unit>`
* Enhancement: Updated to Material Components 1.2.0-beta01.
* Enhancement: Updated to Coroutines 1.3.7.
* Enhancement: Started downgrading alpha versions of AndroidX dependencies to latest stable for upcoming FlowBinding 1.0 release.

## Version 0.11.1

_2020-04-20_

* Fix: revert to AGP 4.1.0-alpha05 which broke publishing.

## Version 0.11.0

_2020-04-20_

* New: Added new binding `fun Slider.changeEvents(emitImmediately: Boolean = false): Flow<SliderChangeEvent>` ([PR](https://github.com/ReactiveCircus/FlowBinding/pull/88)). Thanks [@FlowMo7](https://github.com/FlowMo7) for contribution!
* New: Added **flowbinding-viewpager** artifact with bindings for the legacy `androidx.viewpager.widget.ViewPager`.
* Enhancement: Updated to Kotlin 1.3.72, Coroutines 1.3.5.
* Enhancement: Updated AndroidX:
  * `core:1.3.0-rc01`
  * `appcompat:1.2.0-beta01`
  * `activity:1.2.0-alpha03`
  * `fragment:1.3.0-alpha03`
  * `drawerlayout:1.1.0-beta01`
  * `navigation:2.3.0-alpha05`
  * `preference:1.1.1`
  * `recyclerview:1.2.0-alpha02`
  * `swiperefreshlayout:1.1.0-rc01`
  * `viewpager2:1.1.0-alpha01`
* Enhancement: Updated Material Components to 1.2.0-alpha06.

## Version 0.10.2

_2020-03-14_

* Enhancement: Started tracking API binary compatibility.
* Enhancement: Updated to Kotlin 1.3.70, Coroutines 1.3.4.
* Enhancement: Updated AndroidX:
  * `core:1.3.0-alpha02`
  * `appcompat:1.2.0-alpha03`
  * `activity:1.2.0-alpha01`
  * `fragment:1.3.0-alpha01`
  * `drawerlayout:1.1.0-alpha04`
  * `lifecycle:2.3.0-alpha01`
  * `navigation:2.3.0-alpha03`
  * `swiperefreshlayout:1.1.0-beta01`
* Fix: Do not conflate `Flow<Lifecycle.Event>` from `LifecycleEventFlow` to respect the behavior of `LifecycleObserver`.

## Version 0.10.1

_2020-02-28_

* Fix: Removed incorrect `packagingOptions` exclusion.

## Version 0.10.0

_2020-02-28_

* New: We have a [new website](https://reactivecircus.github.io/FlowBinding/) with automatically generated & updated API docs.
* New: `minSdkVersion` has been dropped to **API 14** to match AndroidX's default and RxBinding.
* Enhancement: Updated AndroidX, Material Components, build tools, Gradle, AGP, detekt.

## Version 0.9.0

_2020-01-24_

* New: Added **flowbinding-lifecycle** artifact with a binding for lifecycle events.
* New: Added **flowbinding-activity** artifact with a binding for back pressed events.
* Enhancement: Updated AndroidX - **activity 1.1.0**, **fragment 1.2.0** and **navigation 2.2.0**.
* Enhancement: Updated **Material Components** to **1.2.0-alpha04**.

## Version 0.8.0

_2020-01-04_

* Fix: Removed incorrect `packagingOptions` exclusion.

## Version 0.7.0

_2019-12-30_

* New: Added **flowbinding-preference** artifact with bindings for the AndroidX Preference library.
* Enhancement: Migrated from [custom GitHub Action](https://github.com/ReactiveCircus/android-emulator-runner) to [Cirrus CI](https://cirrus-ci.org/) for running instrumented tests. Tests now take **~15 mins** compared to **~21 mins** with GitHub Actions.
* Enhancement: Updated Gradle, AGP, AndroidX, Material Components, Kotlin, Coroutines, detekt, kluent, blueprint.
* Enhancement: Stopped generating `BuildConfig` for libraries.

## Version 0.6.0

_2019-11-14_

* New: Added new binding `fun MaterialCardView.checkedChanges(emitImmediately: Boolean = false): Flow<Boolean>` 
* Enhancement: Migrated from **bitrise.io** to a [custom GitHub Action](https://github.com/ReactiveCircus/android-emulator-runner) for running instrumented tests on macOS VMs. Tests now take **~21 mins** compared to **30+ mins** with bitrise.
* Fix: Added `@SdkSuppress` to `ViewScrollChangeEventFlowTest` which requires API 23.
* Enhancement: Updated Gradle, AGP and AndroidX.
* Fix: Added a bunch of missing event listener removals ([PR](https://github.com/ReactiveCircus/FlowBinding/pull/52)). Thanks [@hoc081098](https://github.com/hoc081098) for contribution!
* Enhancement: Added a custom lint check `MissingListenerRemoval` for detecting missing listener removals in `awaitClose` within a `callbackFlow` implementation.

## Version 0.5.0

_2019-10-28_

This is the initial release of FlowBinding - Kotlin Flow binding APIs for Android's platform and unbundled UI widgets.

Please note that while the library is heavily tested with instrumented tests, the APIs are not yet stable. Our plan is to polish the library by adding missing bindings and fixing bugs as we work towards 1.0.

* New: **flowbinding-android** artifact - provides Flow bindings for the Android platform APIs.
* New: **flowbinding-appcompat** artifact - provides Flow bindings for the AndroidX AppCompat library.
* New: **flowbinding-core** artifact - provides Flow bindings for the AndroidX Core library.
* New: **flowbinding-drawerlayout** artifact - provides Flow bindings for the AndroidX DrawerLayout library.
* New: **flowbinding-navigation** artifact - provides Flow bindings for the AndroidX Navigation library.
* New: **flowbinding-recyclerview** artifact - provides Flow bindings for the AndroidX RecyclerView library.
* New: **flowbinding-swiperefreshlayout** artifact - provides Flow bindings for the AndroidX SwipeRefreshLayout library.
* New: **flowbinding-viewpager2** artifact - provides Flow bindings for the AndroidX ViewPager2 library.
