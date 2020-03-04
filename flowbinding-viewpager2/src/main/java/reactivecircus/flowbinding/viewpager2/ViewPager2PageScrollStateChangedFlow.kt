package reactivecircus.flowbinding.viewpager2

import androidx.annotation.CheckResult
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_SETTLING
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of page scroll state change events on the [ViewPager2] instance
 * where the value emitted can be one of [SCROLL_STATE_IDLE], [SCROLL_STATE_DRAGGING]} or [SCROLL_STATE_SETTLING].
 *
 * Note: Created flow keeps a strong reference to the [ViewPager2] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * viewPager2.pageScrollStateChanges()
 *     .onEach { state ->
 *          // handle state
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
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
