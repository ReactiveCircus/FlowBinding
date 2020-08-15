package reactivecircus.flowbinding.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

@Suppress("UnstableApiUsage")
class MissingListenerRemovalDetectorTest {

    @Test
    fun `listener set to null via function call`() {
        lint()
            .files(
                kotlin(
                    """
                fun View.clicks(): Flow<Unit> = callbackFlow {
                    checkMainThread()
                    val listener = View.OnClickListener {
                        safeOffer(Unit)
                    }
                    setOnClickListener(listener)
                    awaitClose { setOnClickListener(null) }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun `listener set to casted null via function call`() {
        lint()
            .files(
                kotlin(
                    """
                fun NestedScrollView.scrollChangeEvents(): Flow<ScrollChangeEvent> = callbackFlow {
                    checkMainThread()
                    val listener = NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                        safeOffer(
                            ScrollChangeEvent(
                                view = v,
                                scrollX = scrollX,
                                scrollY = scrollY,
                                oldScrollX = oldScrollX,
                                oldScrollY = oldScrollY
                            )
                        )
                    }
                    setOnScrollChangeListener(listener)
                    awaitClose { setOnScrollChangeListener(null as NestedScrollView.OnScrollChangeListener?) }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun `listener set to null via field setter`() {
        lint()
            .files(
                kotlin(
                    """
                fun View.focusChanges(): Flow<Boolean> = callbackFlow {
                    checkMainThread()
                    val listener = View.OnFocusChangeListener { _, hasFocus ->
                        safeOffer(hasFocus)
                    }
                    onFocusChangeListener = listener
                    awaitClose { onFocusChangeListener = null }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun `listener set to null via field setter on another field`() {
        lint()
            .files(
                kotlin(
                    """
                fun View.dismisses(): Flow<View> = callbackFlow<View> {
                    checkMainThread()
                    val behavior = params.behavior
                    val listener = object : SwipeDismissBehavior.OnDismissListener {
                        override fun onDismiss(view: View) {
                            safeOffer(view)
                        }
                        override fun onDragStateChanged(state: Int) = Unit
                    }
                    behavior.listener = listener
                    awaitClose { behavior.listener = null }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun `listener removed`() {
        lint()
            .files(
                kotlin(
                    """
                fun View.layoutChanges(): Flow<Unit> = callbackFlow {
                    checkMainThread()
                    val listener =
                        View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                            safeOffer(Unit)
                        }
                    addOnLayoutChangeListener(listener)
                    awaitClose { removeOnLayoutChangeListener(listener) }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun `callback removed`() {
        lint()
            .files(
                kotlin(
                    """
                fun Snackbar.shownEvents(): Flow<Unit> = callbackFlow<Unit> {
                    checkMainThread()
                    val callback = object : Snackbar.Callback() {
                        override fun onShown(sb: Snackbar?) {
                            safeOffer(Unit)
                        }
                    }
                    this@shownEvents.addCallback(callback)
                    awaitClose { removeCallback(callback) }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun `observer removed`() {
        lint()
            .files(
                kotlin(
                    """
                fun Lifecycle.events(): Flow<Lifecycle.Event> = callbackFlow<Lifecycle.Event> {
                    checkMainThread()
                    val observer = object : LifecycleObserver {
                        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
                        fun onEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
                            safeOffer(event)
                        }
                    }
                    addObserver(observer)
                    awaitClose { removeObserver(observer) }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun `callback unregistered`() {
        lint()
            .files(
                kotlin(
                    """
                fun ViewPager2.pageScrollStateChanges(): Flow<Int> = callbackFlow<Int> {
                    checkMainThread()
                    val callback = object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageScrollStateChanged(state: Int) {
                            safeOffer(state)
                        }
                    }
                    registerOnPageChangeCallback(callback)
                    awaitClose { unregisterOnPageChangeCallback(callback) }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun `missing awaitClose`() {
        lint()
            .files(
                kotlin(
                    """
                fun View.clicks(): Flow<Unit> = callbackFlow {
                    checkMainThread()
                    val listener = View.OnClickListener {
                        safeOffer(Unit)
                    }
                    setOnClickListener(listener)
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expect(
                """
                    src/test.kt:1: Error: A listener or callback has been added within the callbackFlow, but it hasn't been removed / unregistered in the awaitClose block. [MissingListenerRemoval]
                    fun View.clicks(): Flow<Unit> = callbackFlow {
                                                    ^
                    1 errors, 0 warnings
            """.trimIndent()
            )
    }

    @Test
    fun `listener set via function call but not set to null in awaitClose`() {
        lint()
            .files(
                kotlin(
                    """
                fun View.clicks(): Flow<Unit> = callbackFlow {
                    checkMainThread()
                    val listener = View.OnClickListener {
                        safeOffer(Unit)
                    }
                    setOnClickListener(listener)
                    awaitClose { setOnClickListener(listener) }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expect(
                """
                    src/test.kt:1: Error: A listener or callback has been added within the callbackFlow, but it hasn't been removed / unregistered in the awaitClose block. [MissingListenerRemoval]
                    fun View.clicks(): Flow<Unit> = callbackFlow {
                                                    ^
                    1 errors, 0 warnings
                """.trimMargin()
            )
    }

    @Test
    fun `listener set via field setter but not set to null in awaitClose`() {
        lint()
            .files(
                kotlin(
                    """
                fun View.focusChanges(): Flow<Boolean> = callbackFlow {
                    checkMainThread()
                    val listener = View.OnFocusChangeListener { _, hasFocus ->
                        safeOffer(hasFocus)
                    }
                    onFocusChangeListener = listener
                    awaitClose { }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expect(
                """
                    src/test.kt:1: Error: A listener or callback has been added within the callbackFlow, but it hasn't been removed / unregistered in the awaitClose block. [MissingListenerRemoval]
                    fun View.focusChanges(): Flow<Boolean> = callbackFlow {
                                                             ^
                    1 errors, 0 warnings
                """.trimMargin()
            )
    }

    @Test
    fun `listener set on a filed via field setter but not set to null in awaitClose`() {
        lint()
            .files(
                kotlin(
                    """
                fun View.dismisses(): Flow<View> = callbackFlow<View> {
                    checkMainThread()
                    val behavior = params.behavior
                    val listener = object : SwipeDismissBehavior.OnDismissListener {
                        override fun onDismiss(view: View) {
                            safeOffer(view)
                        }
                        override fun onDragStateChanged(state: Int) = Unit
                    }
                    behavior.listener = listener
                    awaitClose { }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expect(
                """
                    src/test.kt:1: Error: A listener or callback has been added within the callbackFlow, but it hasn't been removed / unregistered in the awaitClose block. [MissingListenerRemoval]
                    fun View.dismisses(): Flow<View> = callbackFlow<View> {
                                                       ^
                    1 errors, 0 warnings
                """.trimMargin()
            )
    }

    @Test
    fun `listener added but not removed in awaitClose`() {
        lint()
            .files(
                kotlin(
                    """
                fun View.layoutChanges(): Flow<Unit> = callbackFlow {
                    checkMainThread()
                    val listener =
                        View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                            safeOffer(Unit)
                        }
                    addOnLayoutChangeListener(listener)
                    awaitClose { }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expect(
                """
                    src/test.kt:1: Error: A listener or callback has been added within the callbackFlow, but it hasn't been removed / unregistered in the awaitClose block. [MissingListenerRemoval]
                    fun View.layoutChanges(): Flow<Unit> = callbackFlow {
                                                           ^
                    1 errors, 0 warnings
                """.trimMargin()
            )
    }

    @Test
    fun `callback added but not removed in awaitClose`() {
        lint()
            .files(
                kotlin(
                    """
                fun Snackbar.shownEvents(): Flow<Unit> = callbackFlow<Unit> {
                    checkMainThread()
                    val callback = object : Snackbar.Callback() {
                        override fun onShown(sb: Snackbar?) {
                            safeOffer(Unit)
                        }
                    }
                    this@shownEvents.addCallback(callback)
                    awaitClose { }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expect(
                """
                    src/test.kt:1: Error: A listener or callback has been added within the callbackFlow, but it hasn't been removed / unregistered in the awaitClose block. [MissingListenerRemoval]
                    fun Snackbar.shownEvents(): Flow<Unit> = callbackFlow<Unit> {
                                                             ^
                    1 errors, 0 warnings
                """.trimMargin()
            )
    }

    @Test
    fun `callback registered but not unregistered in awaitClose`() {
        lint()
            .files(
                kotlin(
                    """
                fun ViewPager2.pageScrollStateChanges(): Flow<Int> = callbackFlow<Int> {
                    checkMainThread()
                    val callback = object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageScrollStateChanged(state: Int) {
                            safeOffer(state)
                        }
                    }
                    registerOnPageChangeCallback(callback)
                    awaitClose { }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expect(
                """
                    src/test.kt:1: Error: A listener or callback has been added within the callbackFlow, but it hasn't been removed / unregistered in the awaitClose block. [MissingListenerRemoval]
                    fun ViewPager2.pageScrollStateChanges(): Flow<Int> = callbackFlow<Int> {
                                                                         ^
                    1 errors, 0 warnings
                """.trimMargin()
            )
    }

    @Test
    fun `no listener or callback added`() {
        lint()
            .files(
                kotlin(
                    """
                fun View.test(): Flow<Unit> = callbackFlow {
                    val listener = View.OnClickListener {
                        safeOffer(Unit)
                    }
                }.conflate()
                """.trimIndent()
                )
            )
            .allowMissingSdk()
            .issues(MissingListenerRemovalDetector.ISSUE)
            .run()
            .expectClean()
    }
}
