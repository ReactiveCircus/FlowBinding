package reactivecircus.flowbinding.viewpager

import androidx.annotation.CheckResult
import androidx.viewpager.widget.ViewPager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [InitialValueFlow] of page selected events on the [ViewPager] instance
 * where the value emitted is the position index of the new selected page.
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
public fun ViewPager.pageSelections(): InitialValueFlow<Int> = callbackFlow {
    checkMainThread()
    val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) = Unit

        override fun onPageSelected(position: Int) {
            trySend(position)
        }

        override fun onPageScrollStateChanged(state: Int) = Unit
    }

    addOnPageChangeListener(listener)
    awaitClose { removeOnPageChangeListener(listener) }
}
    .conflate()
    .asInitialValueFlow { currentItem }
