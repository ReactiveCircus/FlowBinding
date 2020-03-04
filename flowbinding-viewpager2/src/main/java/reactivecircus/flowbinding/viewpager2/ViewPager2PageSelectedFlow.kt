package reactivecircus.flowbinding.viewpager2

import androidx.annotation.CheckResult
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of page selected events on the [ViewPager2] instance
 * where the value emitted is the position index of the new selected page.
 *
 * @param emitImmediately whether to emit the current value immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [ViewPager2] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * viewPager2.pageSelections()
 *     .onEach { position ->
 *          // handle position
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun ViewPager2.pageSelections(emitImmediately: Boolean = false): Flow<Int> = callbackFlow<Int> {
    checkMainThread()
    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            safeOffer(position)
        }
    }
    registerOnPageChangeCallback(callback)
    awaitClose { unregisterOnPageChangeCallback(callback) }
}
    .startWithCurrentValue(emitImmediately) { currentItem }
    .conflate()
