package reactivecircus.flowbinding.android.widget

import android.widget.CompoundButton
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [InitialValueFlow] of checked state changes on the [CompoundButton] instance
 * where the value emitted is whether the [CompoundButton] is currently checked.
 *
 * Note: Created flow keeps a strong reference to the [CompoundButton] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * compoundButton.checkedChanges()
 *     .onEach { isChecked ->
 *          // handle isChecked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun CompoundButton.checkedChanges(): InitialValueFlow<Boolean> = callbackFlow {
    checkMainThread()
    val listener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        safeOffer(isChecked)
    }
    setOnCheckedChangeListener(listener)
    awaitClose { setOnCheckedChangeListener(null) }
}
    .conflate()
    .asInitialValueFlow { isChecked }
