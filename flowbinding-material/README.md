# FlowBinding Material Components

This module provides bindings for the **Material Components** library.

## Transitive Dependency

`com.google.android.material:material`

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-material:${flowbinding_version}"
```

## Available Bindings

```kotlin
// AppBarLayout
fun AppBarLayout.offsetChanges(): Flow<Int>

// BottomNavigationView
fun BottomNavigationView.itemReselections(): Flow<MenuItem>
fun BottomNavigationView.itemSelections(emitImmediately: Boolean = false): Flow<MenuItem>

// BottomSheetBehavior
fun View.bottomSheetSlides(): Flow<Float>
fun View.bottomSheetStateChanges(): Flow<Int>

// Chip
fun Chip.closeIconClicks(): Flow<Unit>

// ChipGroup
fun ChipGroup.chipCheckedChanges(emitImmediately: Boolean = false): Flow<Int>

// MaterialButton
fun MaterialButton.checkedChanges(): Flow<Boolean>

// MaterialButtonToggleGroup
fun MaterialButtonToggleGroup.buttonCheckedChanges(): Flow<MaterialButtonCheckedChangedEvent>

// MaterialCardView
fun MaterialCardView.checkedChanges(emitImmediately: Boolean = false): Flow<Boolean>

// MaterialDatePicker
fun <S> MaterialDatePicker<S>.cancels(): Flow<Unit>
fun <S> MaterialDatePicker<S>.dismisses(): Flow<Unit>
fun <S> MaterialDatePicker<S>.negativeButtonClicks(): Flow<Unit>
fun <S> MaterialDatePicker<S>.positiveButtonClicks(): Flow<S>

// NavigationView
fun NavigationView.itemSelections(emitImmediately: Boolean = false): Flow<MenuItem>

// Slider
fun Slider.valueChanges(emitImmediately: Boolean = false): Flow<Float>
fun Slider.changeEvents(emitImmediately: Boolean = false): Flow<SliderChangeEvent>
fun Slider.touchEvents(): Flow<SliderTouchEvent>

// RangeSlider
fun RangeSlider.valuesChanges(emitImmediately: Boolean = false): Flow<List<Float>>
fun RangeSlider.changeEvents(emitImmediately: Boolean = false): Flow<RangeSliderChangeEvent>
fun RangeSlider.touchEvents(): Flow<RangeSliderTouchEvent>

// Snackbar
fun Snackbar.dismissEvents(): Flow<Int>
fun Snackbar.shownEvents(): Flow<Unit>

// SwipeDismissBehavior
fun View.dismisses(): Flow<View>
fun View.swipeDismissDragStateChanges(): Flow<Int>

// TabLayout
fun TabLayout.tabSelectionEvents(emitImmediately: Boolean = false): Flow<TabLayoutSelectionEvent>

// TextInputLayout
fun TextInputLayout.startIconClicks(): Flow<Unit>
fun TextInputLayout.endIconClicks(): Flow<Unit>
fun TextInputLayout.errorIconClicks(): Flow<Unit>
fun TextInputLayout.startIconLongClicks(): Flow<Unit>
fun TextInputLayout.endIconLongClicks(): Flow<Unit>
fun TextInputLayout.errorIconLongClicks(): Flow<Unit>
```
