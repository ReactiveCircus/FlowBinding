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
 * Create a [InitialValueFlow] of values change events on the [RangeSlider] instance
 * where the value emitted is the current value of the [RangeSlider].
 *
 * Note: Created flow keeps a strong reference to the [RangeSlider] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * rangeSlider.valuesChanges()
 *     .onEach { values ->
 *          // handle values
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun RangeSlider.valuesChanges(): InitialValueFlow<List<Float>> = callbackFlow {
    checkMainThread()
    val listener = RangeSlider.OnChangeListener { rangeSlider, _, _ ->
        safeOffer(rangeSlider.values)
    }
    addOnChangeListener(listener)
    awaitClose { removeOnChangeListener(listener) }
}
    .conflate()
    .asInitialValueFlow { values }
