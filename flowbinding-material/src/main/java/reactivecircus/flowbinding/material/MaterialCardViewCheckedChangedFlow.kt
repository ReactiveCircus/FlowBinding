package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [InitialValueFlow] of checked state changes on the [MaterialCardView] instance
 * where the value emitted is whether the [MaterialCardView] is currently checked.
 *
 * Note: Created flow keeps a strong reference to the [MaterialCardView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * materialCardView.checkedChanges()
 *     .onEach { isChecked ->
 *          // handle isChecked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun MaterialCardView.checkedChanges(): InitialValueFlow<Boolean> = callbackFlow {
    checkMainThread()
    val listener = MaterialCardView.OnCheckedChangeListener { _, isChecked ->
        trySend(isChecked)
    }
    setOnCheckedChangeListener(listener)
    awaitClose { setOnCheckedChangeListener(null) }
}
    .conflate()
    .asInitialValueFlow { isChecked }
