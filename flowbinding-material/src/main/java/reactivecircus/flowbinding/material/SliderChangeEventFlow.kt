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
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of change events on the [Slider] instance.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [Slider] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * slider.changeEvents()
 *     .onEach { event ->
 *          // handle event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun Slider.changeEvents(emitImmediately: Boolean = false): Flow<SliderChangeEvent> = callbackFlow {
    checkMainThread()
    val listener = Slider.OnChangeListener { slider, value, fromUser ->
        safeOffer(
            SliderChangeEvent(
                slider = slider,
                value = value,
                fromUser = fromUser
            )
        )
    }
    addOnChangeListener(listener)
    awaitClose { removeOnChangeListener(listener) }
}
    .startWithCurrentValue(emitImmediately) {
        SliderChangeEvent(
            slider = this,
            value = value,
            fromUser = false
        )
    }
    .conflate()

class SliderChangeEvent(
    val slider: Slider,
    val value: Float,
    val fromUser: Boolean
)
