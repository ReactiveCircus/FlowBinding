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
fun BottomNavigationView.itemSelections(): InitialValueFlow<MenuItem>

// BottomSheetBehavior
fun View.bottomSheetSlides(): Flow<Float>
fun View.bottomSheetStateChanges(): Flow<Int>

// Chip
fun Chip.closeIconClicks(): Flow<Unit>

// ChipGroup
fun ChipGroup.chipCheckedChanges(): InitialValueFlow<Int>

// MaterialButton
fun MaterialButton.checkedChanges(): Flow<Boolean>

// MaterialButtonToggleGroup
fun MaterialButtonToggleGroup.buttonCheckedChanges(): Flow<MaterialButtonCheckedChangedEvent>

// MaterialCardView
fun MaterialCardView.checkedChanges(): InitialValueFlow<Boolean>

// MaterialDatePicker
fun <S> MaterialDatePicker<S>.cancels(): Flow<Unit>
fun <S> MaterialDatePicker<S>.dismisses(): Flow<Unit>
fun <S> MaterialDatePicker<S>.negativeButtonClicks(): Flow<Unit>
fun <S> MaterialDatePicker<S>.positiveButtonClicks(): Flow<S>

// MaterialTimePicker
fun MaterialTimePicker.cancels(): Flow<Unit>
fun MaterialTimePicker.dismisses(): Flow<Unit>
fun MaterialTimePicker.negativeButtonClicks(): Flow<Unit>
fun MaterialTimePicker.positiveButtonClicks(): Flow<Unit>

// NavigationView
fun NavigationView.itemSelections(): InitialValueFlow<MenuItem>

// Slider
fun Slider.valueChanges(): InitialValueFlow<Float>
fun Slider.changeEvents(): InitialValueFlow<SliderChangeEvent>
fun Slider.touchEvents(): Flow<SliderTouchEvent>

// RangeSlider
fun RangeSlider.valuesChanges(): InitialValueFlow<List<Float>>
fun RangeSlider.changeEvents(): InitialValueFlow<RangeSliderChangeEvent>
fun RangeSlider.touchEvents(): Flow<RangeSliderTouchEvent>

// Snackbar
fun Snackbar.dismissEvents(): Flow<Int>
fun Snackbar.shownEvents(): Flow<Unit>

// SwipeDismissBehavior
fun View.dismisses(): Flow<View>
fun View.swipeDismissDragStateChanges(): Flow<Int>

// TabLayout
fun TabLayout.tabSelectionEvents(): InitialValueFlow<TabLayoutSelectionEvent>

// TextInputLayout
fun TextInputLayout.startIconClicks(): Flow<Unit>
fun TextInputLayout.endIconClicks(): Flow<Unit>
fun TextInputLayout.errorIconClicks(): Flow<Unit>
fun TextInputLayout.startIconLongClicks(): Flow<Unit>
fun TextInputLayout.endIconLongClicks(): Flow<Unit>
fun TextInputLayout.errorIconLongClicks(): Flow<Unit>
```
