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
 * Create a [Flow] of values change events on the [RangeSlider] instance
 * where the value emitted is the current value of the [RangeSlider].
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
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
fun RangeSlider.valuesChanges(emitImmediately: Boolean = false): Flow<List<Float>> = callbackFlow {
    checkMainThread()
    val listener = RangeSlider.OnChangeListener { rangeSlider, _, _ ->
        safeOffer(rangeSlider.values)
    }
    addOnChangeListener(listener)
    awaitClose { removeOnChangeListener(listener) }
}
    .startWithCurrentValue(emitImmediately) { values }
    .conflate()
