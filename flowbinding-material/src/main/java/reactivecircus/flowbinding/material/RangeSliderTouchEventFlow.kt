package reactivecircus.flowbinding.material

import android.annotation.SuppressLint
import androidx.annotation.CheckResult
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of touch events on the [RangeSlider] instance.
 *
 * Note: Created flow keeps a strong reference to the [RangeSlider] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * rangeSlider.touchEvents()
 *     .onEach { event ->
 *          // handle event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun RangeSlider.touchEvents(): Flow<RangeSliderTouchEvent> = callbackFlow {
    checkMainThread()
    val listener = object : RangeSlider.OnSliderTouchListener {
        @SuppressLint("RestrictedApi")
        override fun onStartTrackingTouch(rangeSlider: RangeSlider) {
            trySend(RangeSliderTouchEvent.StartTracking(rangeSlider))
        }

        @SuppressLint("RestrictedApi")
        override fun onStopTrackingTouch(rangeSlider: RangeSlider) {
            trySend(RangeSliderTouchEvent.StopTracking(rangeSlider))
        }
    }
    addOnSliderTouchListener(listener)
    awaitClose { removeOnSliderTouchListener(listener) }
}.conflate()

public sealed class RangeSliderTouchEvent {
    public abstract val rangeSlider: RangeSlider

    public data class StartTracking(override val rangeSlider: RangeSlider) : RangeSliderTouchEvent()

    public data class StopTracking(override val rangeSlider: RangeSlider) : RangeSliderTouchEvent()
}
