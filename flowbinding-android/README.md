# FlowBinding Android

This module provides bindings for the Android platform APIs.

## Download

```groovy
implementation "io.github.reactivecircus.flowbinding:flowbinding-android:${flowbinding_version}"
```

## Available Bindings

```kotlin
// MenuItem
fun MenuItem.actionViewEvents(handled: (MenuItemActionViewEvent) -> Boolean): Flow<MenuItemActionViewEvent>
fun MenuItem.clicks(handled: (MenuItem) -> Boolean): Flow<Unit>

// View
fun View.attachEvents(): Flow<ViewAttachEvent>
fun View.clicks(): Flow<Unit>
fun View.drags(handled: (DragEvent) -> Boolean = { true }): Flow<DragEvent>
fun View.draws(): Flow<Unit>
fun View.focusChanges(): InitialValueFlow<Boolean>
fun View.globalLayouts(): Flow<Unit>
fun ViewGroup.hierarchyChangeEvents(): Flow<HierarchyChangeEvent>
fun View.hovers(handled: (MotionEvent) -> Boolean = { true }): Flow<MotionEvent>
fun View.keys(handled: (KeyEvent) -> Boolean = { true }): Flow<KeyEvent>
fun View.layoutChangeEvents(): Flow<LayoutChangeEvent>
fun View.layoutChanges(): Flow<Unit>
fun View.longClicks(): Flow<Unit>
fun View.preDraws(proceedDrawingPass: () -> Boolean): Flow<Unit>
fun View.scrollChangeEvents(): Flow<ScrollChangeEvent>
fun View.systemUiVisibilityChanges(): Flow<Int>
fun View.touches(handled: (MotionEvent) -> Boolean = { true }): Flow<MotionEvent>

// AbsListView
fun AbsListView.scrollEvents(): Flow<ScrollEvent>

// Adapter
fun Adapter.dataChanges(): InitialValueFlow<Adapter>

// AdapterView
fun <T : Adapter> AdapterView<T>.itemClickEvents(): Flow<AdapterViewItemClickEvent>
fun <T : Adapter> AdapterView<T>.itemClicks(): Flow<Int>
fun <T : Adapter> AdapterView<T>.itemLongClickEvents(handled: (AdapterViewItemLongClickEvent) -> Boolean = { true }): Flow<AdapterViewItemLongClickEvent>
fun <T : Adapter> AdapterView<T>.itemLongClicks(handled: () -> Boolean = { true }): Flow<Int>
fun <T : Adapter> AdapterView<T>.itemSelections(): InitialValueFlow<Int>
fun <T : Adapter> AdapterView<T>.selectionEvents(): InitialValueFlow<AdapterViewSelectionEvent>

// AutoCompleteTextView
fun AutoCompleteTextView.dismisses(): Flow<Unit>
fun AutoCompleteTextView.itemClickEvents(): Flow<AdapterViewItemClickEvent>

// CompoundButton
fun CompoundButton.checkedChanges(): InitialValueFlow<Boolean>

// PopupMenu
fun PopupMenu.dismisses(): Flow<Unit>
fun PopupMenu.itemClicks(): Flow<MenuItem>

// RadioGroup
fun RadioGroup.checkedChanges(): InitialValueFlow<Int>

// RatingBar
fun RatingBar.ratingChangeEvents(): InitialValueFlow<RatingChangeEvent>
fun RatingBar.ratingChanges(): InitialValueFlow<Float>

// SearchView
fun SearchView.queryTextChanges(): InitialValueFlow<CharSequence>
fun SearchView.queryTextEvents(): InitialValueFlow<QueryTextEvent>

// SeekBar
fun SeekBar.changeEvents(): InitialValueFlow<SeekBarChangeEvent>
fun SeekBar.progressChanges(): InitialValueFlow<Int>

// TextView
fun TextView.afterTextChanges(): InitialValueFlow<AfterTextChangeEvent>
fun TextView.beforeTextChanges(): InitialValueFlow<BeforeTextChangeEvent>
fun TextView.editorActionEvents(handled: (EditorActionEvent) -> Boolean = { true }): Flow<EditorActionEvent>
fun TextView.textChangeEvents(): InitialValueFlow<TextChangeEvent>
fun TextView.textChanges(): InitialValueFlow<CharSequence>
```
