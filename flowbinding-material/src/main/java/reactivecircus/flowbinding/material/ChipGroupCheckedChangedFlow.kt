@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of chip checked state change events on the [ChipGroup] instance
 * where the value emitted is the currently checked chip id.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [ChipGroup] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * chipGroup.chipCheckedChanges()
 *     .onEach { checkedId ->
 *          // handle checkedId
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun ChipGroup.chipCheckedChanges(emitImmediately: Boolean = false): Flow<Int> = callbackFlow {
    checkMainThread()
    val listener = ChipGroup.OnCheckedChangeListener { _, checkedId ->
        safeOffer(checkedId)
    }
    setOnCheckedChangeListener(listener)
    awaitClose { setOnCheckedChangeListener(null) }
}
    .startWithCurrentValue(emitImmediately) { checkedChipId }
    .conflate()
