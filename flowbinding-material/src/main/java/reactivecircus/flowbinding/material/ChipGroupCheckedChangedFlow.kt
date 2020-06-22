@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [InitialValueFlow] of chip checked state change events on the [ChipGroup] instance
 * where the value emitted is the currently checked chip id, or [View#NO_ID] when selection is cleared.
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
@OptIn(ExperimentalCoroutinesApi::class)
fun ChipGroup.chipCheckedChanges(): InitialValueFlow<Int> = callbackFlow {
    checkMainThread()
    val listener = ChipGroup.OnCheckedChangeListener { _, checkedId ->
        safeOffer(checkedId)
    }
    setOnCheckedChangeListener(listener)
    awaitClose { setOnCheckedChangeListener(null) }
}
    .conflate()
    .asInitialValueFlow { checkedChipId }
