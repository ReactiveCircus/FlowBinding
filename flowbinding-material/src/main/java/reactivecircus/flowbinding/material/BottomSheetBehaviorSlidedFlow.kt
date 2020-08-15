package reactivecircus.flowbinding.material

import android.view.View
import androidx.annotation.CheckResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of bottom sheet slided events on the [View] instance with a [BottomSheetBehavior]
 * where the value emitted is the slide offset.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.bottomSheetSlides()
 *     .onEach { state ->
 *          // handle state
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun View.bottomSheetSlides(): Flow<Float> = callbackFlow<Float> {
    checkMainThread()
    val behavior = BottomSheetBehavior.from(this@bottomSheetSlides)
    val callback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) = Unit

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            safeOffer(slideOffset)
        }
    }
    behavior.addBottomSheetCallback(callback)
    awaitClose { behavior.removeBottomSheetCallback(callback) }
}.conflate()
