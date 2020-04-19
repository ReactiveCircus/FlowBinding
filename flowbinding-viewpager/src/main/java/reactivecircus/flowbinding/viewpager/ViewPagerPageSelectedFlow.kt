package reactivecircus.flowbinding.viewpager

import androidx.annotation.CheckResult
import androidx.viewpager.widget.ViewPager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of page selected events on the [ViewPager] instance
 * where the value emitted is the position index of the new selected page.
 *
 * @param emitImmediately whether to emit the current value immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [ViewPager] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * viewPager.pageSelections()
 *     .onEach { position ->
 *          // handle position
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun ViewPager.pageSelections(emitImmediately: Boolean = false): Flow<Int> = callbackFlow<Int> {
    checkMainThread()
    val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) = Unit

        override fun onPageSelected(position: Int) {
            safeOffer(position)
        }

        override fun onPageScrollStateChanged(state: Int) = Unit
    }

    addOnPageChangeListener(listener)
    awaitClose { removeOnPageChangeListener(listener) }
}
    .startWithCurrentValue(emitImmediately) { currentItem }
    .conflate()
