package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of checked state changes on the [MaterialCardView] instance
 * where the value emitted is whether the [MaterialCardView] is currently checked.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
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
fun MaterialCardView.checkedChanges(emitImmediately: Boolean = false): Flow<Boolean> = callbackFlow {
    checkMainThread()
    val listener = MaterialCardView.OnCheckedChangeListener { _, isChecked ->
        safeOffer(isChecked)
    }
    setOnCheckedChangeListener(listener)
    awaitClose { setOnCheckedChangeListener(null) }
}
    .startWithCurrentValue(emitImmediately) { isChecked }
    .conflate()
