# Change Log

## Version 0.9.0

_2020-01-24_

* New: Added **flowbinding-lifecycle** artifact with a binding for lifecycle events.
* New: Added **flowbinding-activity** artifact with a binding for back pressed events.
* Enhancement: Updated AndroidX - **activity 1.1.0**, **fragment 1.2.0** and **navigation 2.2.0**.
* Enhancement: Updated Material Components to 1.2.0-alpha04.

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
* Fix: Added a bunch of missing event listener removals ([PR](https://github.com/ReactiveCircus/FlowBinding/pull/52)). Thanks @hoc081098 for contribution!
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
