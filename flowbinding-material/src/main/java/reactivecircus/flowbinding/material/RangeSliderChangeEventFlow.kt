package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of change events on the [RangeSlider] instance.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [RangeSlider] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * rangeSlider.changeEvents()
 *     .onEach { event ->
 *          // handle event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun RangeSlider.changeEvents(emitImmediately: Boolean = false): Flow<RangeSliderChangeEvent> = callbackFlow {
    checkMainThread()
    val listener = RangeSlider.OnChangeListener { rangeSlider, _, fromUser ->
        safeOffer(
            RangeSliderChangeEvent(
                rangeSlider = rangeSlider,
                values = rangeSlider.values,
                fromUser = fromUser
            )
        )
    }
    addOnChangeListener(listener)
    awaitClose { removeOnChangeListener(listener) }
}
    .startWithCurrentValue(emitImmediately) {
        RangeSliderChangeEvent(
            rangeSlider = this,
            values = this.values,
            fromUser = false
        )
    }
    .conflate()

class RangeSliderChangeEvent(
    val rangeSlider: RangeSlider,
    val values: List<Float>,
    val fromUser: Boolean
)
