package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.slider.Slider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of touch events on the [Slider] instance.
 *
 * Note: Created flow keeps a strong reference to the [Slider] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * slider.touchEvents()
 *     .onEach { event ->
 *          // handle event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun Slider.touchEvents(): Flow<SliderTouchEvent> = callbackFlow<SliderTouchEvent> {
    checkMainThread()
    val listener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {
            safeOffer(SliderTouchEvent.StartTracking(slider))
        }

        override fun onStopTrackingTouch(slider: Slider) {
            safeOffer(SliderTouchEvent.StopTracking(slider))
        }
    }
    addOnSliderTouchListener(listener)
    awaitClose { removeOnSliderTouchListener(listener) }
}.conflate()

public sealed class SliderTouchEvent {
    public abstract val slider: Slider

    public data class StartTracking(override val slider: Slider) : SliderTouchEvent()

    public data class StopTracking(override val slider: Slider) : SliderTouchEvent()
}
