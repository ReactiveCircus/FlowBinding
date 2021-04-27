package reactivecircus.flowbinding.viewpager2

import androidx.annotation.CheckResult
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [InitialValueFlow] of page selected events on the [ViewPager2] instance
 * where the value emitted is the position index of the new selected page.
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
public fun ViewPager2.pageSelections(): InitialValueFlow<Int> = callbackFlow {
    checkMainThread()
    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            trySend(position)
        }
    }
    registerOnPageChangeCallback(callback)
    awaitClose { unregisterOnPageChangeCallback(callback) }
}
    .conflate()
    .asInitialValueFlow { currentItem }
