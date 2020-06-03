package reactivecircus.flowbinding.material

import android.view.View
import androidx.annotation.CheckResult
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of negative button click events on the [MaterialTimePicker] instance
 * when the user clicks the cancel button.
 *
 * Note: Created flow keeps a strong reference to the [MaterialTimePicker] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * timePicker.negativeButtonClicks()
 *     .onEach {
 *          // handle time picker negative button clicked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun MaterialTimePicker.negativeButtonClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnClickListener {
        safeOffer(Unit)
    }
    addOnNegativeButtonClickListener(listener)
    awaitClose { removeOnNegativeButtonClickListener(listener) }
}.conflate()
