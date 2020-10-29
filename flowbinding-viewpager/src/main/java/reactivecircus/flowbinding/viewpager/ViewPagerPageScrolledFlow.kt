@file:Suppress("MatchingDeclarationName")

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

/**
 * Create a [Flow] of page scroll events on the [ViewPager] instance.
 *
 * Note: Created flow keeps a strong reference to the [ViewPager] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * viewPager.pageScrollEvents()
 *     .onEach { event ->
 *          // handle page scroll event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun ViewPager.pageScrollEvents(): Flow<ViewPagerPageScrollEvent> = callbackFlow<ViewPagerPageScrollEvent> {
    checkMainThread()
    val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            safeOffer(
                ViewPagerPageScrollEvent(
                    view = this@pageScrollEvents,
                    position = position,
                    positionOffset = positionOffset,
                    positionOffsetPixel = positionOffsetPixels
                )
            )
        }

        override fun onPageSelected(position: Int) = Unit

        override fun onPageScrollStateChanged(state: Int) = Unit
    }
    addOnPageChangeListener(listener)
    awaitClose { removeOnPageChangeListener(listener) }
}.conflate()

public data class ViewPagerPageScrollEvent(
    public val view: ViewPager,
    public val position: Int,
    public val positionOffset: Float,
    public val positionOffsetPixel: Int
)
