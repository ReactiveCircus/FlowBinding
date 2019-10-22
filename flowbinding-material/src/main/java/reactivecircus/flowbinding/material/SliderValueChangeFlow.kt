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
 * Create a [Flow] of value change events on the [Slider] instance
 * where the value emitted is the current value of the [Slider].
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [Slider] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * slider.valueChanges()
 *     .onEach { value ->
 *          // handle value
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun Slider.valueChanges(emitImmediately: Boolean = false): Flow<Float> = callbackFlow {
    checkMainThread()
    val listener = Slider.OnChangeListener { _, value ->
        safeOffer(value)
    }
    setOnChangeListener(listener)
    awaitClose { setOnChangeListener(null) }
}
    .startWithCurrentValue(emitImmediately) { value }
    .conflate()
