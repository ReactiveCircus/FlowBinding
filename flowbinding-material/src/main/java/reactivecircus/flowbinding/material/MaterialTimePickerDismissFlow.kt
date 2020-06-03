package reactivecircus.flowbinding.material

import android.content.DialogInterface
import androidx.annotation.CheckResult
import androidx.fragment.app.DialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of dismiss events on the [MaterialTimePicker] instance.
 * This emits whenever the underlying [DialogFragment] is dismissed, no matter how it is dismissed.
 *
 * Note: Created flow keeps a strong reference to the [MaterialTimePicker] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * timePicker.dismisses()
 *     .onEach {
 *          // handle time picker dismissed
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun MaterialTimePicker.dismisses(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = DialogInterface.OnDismissListener {
        safeOffer(Unit)
    }
    addOnDismissListener(listener)
    awaitClose { removeOnDismissListener(listener) }
}.conflate()
