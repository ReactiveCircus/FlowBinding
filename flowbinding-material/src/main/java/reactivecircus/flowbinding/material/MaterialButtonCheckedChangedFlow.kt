package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.offerIfNotClosed

/**
 * Create a [Flow] of material button checked state change events on the [MaterialButton] instance
 * where the value emitted is whether the button is currently checked.
 *
 * Note: Created flow keeps a strong reference to the [MaterialButton] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * materialButton.checkedChanges()
 *     .onEach { isChecked ->
 *          // handle isChecked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun MaterialButton.checkedChanges(): Flow<Boolean> =
    callbackFlow {
        checkMainThread()
        val listener = MaterialButton.OnCheckedChangeListener { _, isChecked ->
            offerIfNotClosed(isChecked)
        }
        addOnCheckedChangeListener(listener)
        awaitClose { removeOnCheckedChangeListener(listener) }
    }.conflate()
