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
 * Create a [InitialValueFlow] of value change events on the [Slider] instance
 * where the value emitted is the current value of the [Slider].
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
@OptIn(ExperimentalCoroutinesApi::class)
public fun Slider.valueChanges(): InitialValueFlow<Float> = callbackFlow {
    checkMainThread()
    val listener = Slider.OnChangeListener { _, value, _ ->
        safeOffer(value)
    }
    addOnChangeListener(listener)
    awaitClose { removeOnChangeListener(listener) }
}
    .conflate()
    .asInitialValueFlow { value }
