# FlowBinding

[![CircleCI](https://circleci.com/gh/ReactiveCircus/FlowBinding.svg?style=svg)](https://circleci.com/gh/ReactiveCircus/FlowBinding) [![Build Status](https://app.bitrise.io/app/6ff0212a079f16f3/status.svg?token=dtE8nQVs12zS4l61-fJfFw&branch=master)](https://app.bitrise.io/app/6ff0212a079f16f3) [![Android API](https://img.shields.io/badge/API-21%2B-blue.svg?label=API&maxAge=300)](https://www.android.com/history/) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Kotlin Flow binding APIs for Android's platform and unbundled UI widgets, inspired by [RxBinding][rxbinding].

[Flow][flow] is (conceptually) a reactive streams implementation provided by the [kotlinx-coroutines-core][kotlinx-coroutines] artifact.

**FlowBinding** offers an extensive set of extension functions that turn traditional callbacks / listeners on Android UI widgets into the `Flow` type.

The binding implementation respects the `CoroutineScope` used for **collecting** the flows by unregistering the callback / listener automatically when the scope is cancelled.

## Roadmap

### Platform Bindings

- [x] View
    - [x] `fun MenuItem.actionViewEvents(handled: (MenuItemActionViewEvent) -> Boolean): Flow<MenuItemActionViewEvent>`
    - [x] `fun MenuItem.clicks(handled: (MenuItem) -> Boolean): Flow<Unit>`
    - [x] `fun View.attachEvents(): Flow<ViewAttachEvent>`
    - [x] `fun View.clicks(): Flow<Unit>`
    - [x] `fun View.drags(handled: (DragEvent) -> Boolean = { true }): Flow<DragEvent>`
    - [x] `fun View.draws(): Flow<Unit>`
    - [x] `fun View.focusChanges(emitImmediately: Boolean = false): Flow<Boolean>`
    - [x] `fun View.globalLayouts(): Flow<Unit>`
    - [x] `fun ViewGroup.hierarchyChangeEvents(): Flow<HierarchyChangeEvent>`
    - [x] `fun View.hovers(handled: (MotionEvent) -> Boolean = { true }): Flow<MotionEvent>`
    - [x] `fun View.keys(handled: (KeyEvent) -> Boolean = { true }): Flow<KeyEvent>`
    - [x] `fun View.layoutChangeEvents(): Flow<LayoutChangeEvent>`
    - [x] `fun View.layoutChanges(): Flow<Unit>`
    - [x] `fun View.longClicks(): Flow<Unit>`
    - [x] `fun View.preDraws(proceedDrawingPass: () -> Boolean): Flow<Unit>`
    - [x] `fun View.scrollChangeEvents(): Flow<ScrollChangeEvent>`
    - [x] `fun View.systemUiVisibilityChanges(): Flow<Int>`
    - [x] `fun View.touches(handled: (MotionEvent) -> Boolean = { true }): Flow<MotionEvent>`
- [x] Widget
    - [x] `fun AbsListView.scrollEvents(): Flow<ScrollEvent>`
    - [x] `fun Adapter.dataChanges(emitImmediately: Boolean = false): Flow<Adapter>`
    - [x] `fun <T : Adapter> AdapterView<T>.itemClickEvents(): Flow<AdapterViewItemClickEvent>`
    - [x] `fun <T : Adapter> AdapterView<T>.itemClicks(): Flow<Int>`
    - [x] `fun <T : Adapter> AdapterView<T>.itemLongClickEvents(handled: (AdapterViewItemLongClickEvent) -> Boolean = { true }): Flow<AdapterViewItemLongClickEvent>`
    - [x] `fun <T : Adapter> AdapterView<T>.itemLongClicks(handled: () -> Boolean = { true }): Flow<Int>`
    - [x] `fun <T : Adapter> AdapterView<T>.itemSelections(emitImmediately: Boolean = false): Flow<Int>`
    - [x] `fun <T : Adapter> AdapterView<T>.selectionEvents(emitImmediately: Boolean = false): Flow<AdapterViewSelectionEvent>`
    - [x] `fun AutoCompleteTextView.dismisses(): Flow<Unit>`
    - [x] `fun AutoCompleteTextView.itemClickEvents(): Flow<AdapterViewItemClickEvent>`
    - [x] `fun CompoundButton.checkedChanges(emitImmediately: Boolean = false): Flow<Boolean>`
    - [x] `fun PopupMenu.dismisses(): Flow<Unit>`
    - [x] `fun PopupMenu.itemClicks(): Flow<MenuItem>`
    - [x] `fun RadioGroup.checkedChanges(emitImmediately: Boolean = false): Flow<Int>`
    - [x] `fun RatingBar.ratingChangeEvents(emitImmediately: Boolean = false): Flow<RatingChangeEvent>`
    - [x] `fun RatingBar.ratingChanges(emitImmediately: Boolean = false): Flow<Float>`
    - [x] `fun SearchView.queryTextChanges(emitImmediately: Boolean = false): Flow<CharSequence>`
    - [x] `fun SearchView.queryTextEvents(emitImmediately: Boolean = false): Flow<QueryTextEvent>`
    - [x] `fun SeekBar.changeEvents(emitImmediately: Boolean = false): Flow<SeekBarChangeEvent>`
    - [x] `fun SeekBar.progressChanges(emitImmediately: Boolean = false): Flow<Int>`
    - [x] `fun TextView.afterTextChanges(emitImmediately: Boolean = false): Flow<AfterTextChangeEvent>`
    - [x] `fun TextView.beforeTextChanges(emitImmediately: Boolean = false): Flow<BeforeTextChangeEvent>`
    - [x] `fun TextView.editorActionEvents(handled: (EditorActionEvent) -> Boolean = { true }): Flow<EditorActionEvent>`
    - [x] `fun TextView.textChangeEvents(emitImmediately: Boolean = false): Flow<TextChangeEvent>`
    - [x] `fun TextView.textChanges(emitImmediately: Boolean = false): Flow<CharSequence>`

### Material Components Bindings

- [x] AppBarLayout
    - [x] `fun AppBarLayout.offsetChanges(): Flow<Int>`
- [x] BottomNavigationView
    - [x] `fun BottomNavigationView.itemReselections(): Flow<MenuItem>`
    - [x] `fun BottomNavigationView.itemSelections(emitImmediately: Boolean = false): Flow<MenuItem>`
- [x] BottomSheetBehavior
    - [x] `fun View.bottomSheetSlides(): Flow<Float>`
    - [x] `fun View.bottomSheetStateChanges(): Flow<Int>`
- [x] Chip
    - [x] `fun Chip.closeIconClicks(): Flow<Unit>`
- [x] ChipGroup
    - [x] `fun ChipGroup.chipCheckedChanges(emitImmediately: Boolean = false): Flow<Int>`
- [x] MaterialButton
    - [x] `fun MaterialButton.checkedChanges(): Flow<Boolean>`
- [x] MaterialButtonToggleGroup
    - [x] `fun MaterialButtonToggleGroup.buttonCheckedChanges(): Flow<MaterialButtonCheckedChangedEvent>`
- [x] MaterialDatePicker
    - [x] `fun <S> MaterialDatePicker<S>.cancels(): Flow<Unit>`
    - [x] `fun <S> MaterialDatePicker<S>.dismisses(): Flow<Unit>`
    - [x] `fun <S> MaterialDatePicker<S>.negativeButtonClicks(): Flow<Unit>`
    - [x] `fun <S> MaterialDatePicker<S>.positiveButtonClicks(): Flow<S>`    
- [x] NavigationView
    - [x] `fun NavigationView.itemSelections(emitImmediately: Boolean = false): Flow<MenuItem>`
- [x] Slider
    - [x] `fun Slider.valueChanges(emitImmediately: Boolean = false): Flow<Float>`
- [x] Snackbar
    - [x] `fun Snackbar.dismissEvents(): Flow<Int>`
    - [x] `fun Snackbar.shownEvents(): Flow<Unit>`
- [x] SwipeDismissBehavior
    - [x] `fun View.dismisses(): Flow<View>`
    - [x] `fun View.swipeDismissDragStateChanges(): Flow<Int>`
- [x] TabLayout
    - [x] `fun TabLayout.tabSelectionEvents(emitImmediately: Boolean = false): Flow<TabLayoutSelectionEvent>`
- [x] TextInputLayout
    - [x] `fun TabLayout.textInputLayoutStartIconClicks(): Flow<Unit>`
    - [x] `fun TabLayout.textInputLayoutEndIconClicks(): Flow<Unit>`
    - [x] `fun TabLayout.textInputLayoutStartIconLongClicks(): Flow<Unit>`
    - [x] `fun TabLayout.textInputLayoutEndIconLongClicks(): Flow<Unit>`

### AndroidX Bindings

- [x] AppCompat
    - [x] `fun ActionMenuView.itemClicks(): Flow<MenuItem>`
    - [x] `fun PopupMenu.dismisses(): Flow<Unit>`
    - [x] `fun PopupMenu.itemClicks(): Flow<MenuItem>`
    - [x] `fun SearchView.queryTextChanges(emitImmediately: Boolean = false): Flow<CharSequence>`
    - [x] `fun SearchView.queryTextEvents(emitImmediately: Boolean = false): Flow<QueryTextEvent>`
    - [x] `fun Toolbar.itemClicks(): Flow<MenuItem>`
    - [x] `fun Toolbar.navigationClicks(): Flow<Unit>`

- [x] Core
    - [x] `fun NestedScrollView.scrollChangeEvents(): Flow<ScrollChangeEvent>`

- [x] DrawerLayout
    - [x] `fun DrawerLayout.drawerStateChanges(gravity: Int, emitImmediately: Boolean = false): Flow<Boolean>`

- [x] RecyclerView
    - [x] `fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> T.dataChanges(emitImmediately: Boolean = false): Flow<T>`
    - [x] `fun RecyclerView.childAttachStateChangeEvents(): Flow<RecyclerViewChildAttachStateChangeEvent>`
    - [x] `fun RecyclerView.flingEvents(handled: (FlingEvent) -> Boolean = { true }): Flow<FlingEvent>`
    - [x] `fun RecyclerView.scrollEvents(): Flow<RecyclerViewScrollEvent>`
    - [x] `fun RecyclerView.scrollStateChanges(): Flow<Int>`

- [x] SwipeRefreshLayout
    - [x] `fun SwipeRefreshLayout.refreshes(): Flow<Unit>`

- [x] ViewPager 2
    - [x] `fun ViewPager2.pageScrollEvents(): Flow<ViewPager2PageScrollEvent>`
    - [x] `fun ViewPager2.pageScrollStateChanges(): Flow<Int>`
    - [x] `fun ViewPager2.pageSelections(emitImmediately: Boolean = false): Flow<Int>`

- [x] Navigation Component
    - [x] `fun NavController.destinationChangeEvents(): Flow<DestinationChangeEvent>`

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

[maven-central]: https://search.maven.org/search?q=g:io.github.reactivecircus.flowbinding
[snap]: https://oss.sonatype.org/content/repositories/snapshots/
[rxbinding]: https://github.com/JakeWharton/RxBinding
[flow]: https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/
[kotlinx-coroutines]: https://github.com/Kotlin/kotlinx.coroutines
