package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [InitialValueFlow] of change events on the [RangeSlider] instance.
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
public fun RangeSlider.changeEvents(): InitialValueFlow<RangeSliderChangeEvent> =
    callbackFlow {
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
        .conflate()
        .asInitialValueFlow {
            RangeSliderChangeEvent(
                rangeSlider = this,
                values = this.values,
                fromUser = false
            )
        }

public data class RangeSliderChangeEvent(
    public val rangeSlider: RangeSlider,
    public val values: List<Float>,
    public val fromUser: Boolean
)
