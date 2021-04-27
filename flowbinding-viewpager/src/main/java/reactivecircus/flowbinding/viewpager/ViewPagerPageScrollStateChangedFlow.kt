package reactivecircus.flowbinding.viewpager

import androidx.annotation.CheckResult
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING
import androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE
import androidx.viewpager.widget.ViewPager.SCROLL_STATE_SETTLING
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of page scroll state change events on the [ViewPager] instance
 * where the value emitted can be one of [SCROLL_STATE_IDLE], [SCROLL_STATE_DRAGGING]} or [SCROLL_STATE_SETTLING].
 *
 * Note: Created flow keeps a strong reference to the [ViewPager] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * viewPager.pageScrollStateChanges()
 *     .onEach { state ->
 *          // handle state
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun ViewPager.pageScrollStateChanges(): Flow<Int> = callbackFlow {
    checkMainThread()
    val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) = Unit

        override fun onPageSelected(position: Int) = Unit

        override fun onPageScrollStateChanged(state: Int) {
            trySend(state)
        }
    }

    addOnPageChangeListener(listener)
    awaitClose { removeOnPageChangeListener(listener) }
}.conflate()
