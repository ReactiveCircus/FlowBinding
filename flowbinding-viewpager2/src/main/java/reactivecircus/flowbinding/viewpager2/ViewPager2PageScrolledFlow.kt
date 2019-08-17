@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.viewpager2

import androidx.annotation.CheckResult
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.offerIfNotClosed

/**
 * Create a [Flow] of page scroll events on the [ViewPager2] instance.
 *
 * Note: Created flow keeps a strong reference to the [ViewPager2] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * viewPager2.pageScrollEvents()
 *     .onEach { pageScrollEvent ->
 *          // handle pageScrollEvent
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun ViewPager2.pageScrollEvents(): Flow<ViewPager2PageScrollEvent> =
    callbackFlow<ViewPager2PageScrollEvent> {
        checkMainThread()
        val callback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                offerIfNotClosed(
                    ViewPager2PageScrollEvent(
                        position, positionOffset, positionOffsetPixels
                    )
                )
            }
        }
        registerOnPageChangeCallback(callback)
        awaitClose { unregisterOnPageChangeCallback(callback) }
    }.conflate()

data class ViewPager2PageScrollEvent(
    val position: Int,
    val positionOffset: Float,
    val positionOffsetPixel: Int
)
