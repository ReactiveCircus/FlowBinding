package reactivecircus.flowbinding.material

import android.content.DialogInterface
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
 * Create a [Flow] of cancel events on the [MaterialTimePicker] instance.
 * This emits when the user cancels the picker via back button or a touch outside the view.
 * It does not emit when the user clicks the cancel button.
 * To get a [Flow] of using clicking the cancel button, use the [MaterialTimePicker.negativeButtonClicks()] binding.
 *
 * Note: Created flow keeps a strong reference to the [MaterialTimePicker] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * timePicker.cancels()
 *     .onEach {
 *          // handle time picker canceled
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun MaterialTimePicker.cancels(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = DialogInterface.OnCancelListener {
        safeOffer(Unit)
    }
    addOnCancelListener(listener)
    awaitClose { removeOnCancelListener(listener) }
}.conflate()
