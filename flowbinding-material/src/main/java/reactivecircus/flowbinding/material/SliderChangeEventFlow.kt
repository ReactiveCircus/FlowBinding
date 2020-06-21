package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.slider.Slider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [InitialValueFlow] of change events on the [Slider] instance.
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
fun Slider.changeEvents(): InitialValueFlow<SliderChangeEvent> = callbackFlow {
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
    .conflate()
    .asInitialValueFlow {
        SliderChangeEvent(
            slider = this,
            value = value,
            fromUser = false
        )
    }

class SliderChangeEvent(
    val slider: Slider,
    val value: Float,
    val fromUser: Boolean
)
