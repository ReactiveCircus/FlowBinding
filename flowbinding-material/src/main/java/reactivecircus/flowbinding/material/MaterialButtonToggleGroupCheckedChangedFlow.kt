@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of material button checked state change events on the [MaterialButtonToggleGroup] instance.
 *
 * Note: Created flow keeps a strong reference to the [MaterialButtonToggleGroup] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * materialButtonToggleGroup.buttonCheckedChanges()
 *     .onEach { event ->
 *          // handle button checked event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun MaterialButtonToggleGroup.buttonCheckedChanges(): Flow<MaterialButtonCheckedChangedEvent> = callbackFlow {
    checkMainThread()
    val listener = MaterialButtonToggleGroup.OnButtonCheckedListener { _, checkedId, isChecked ->
        trySend(
            MaterialButtonCheckedChangedEvent(checkedId, isChecked)
        )
    }
    addOnButtonCheckedListener(listener)
    awaitClose { removeOnButtonCheckedListener(listener) }
}.conflate()

public data class MaterialButtonCheckedChangedEvent(
    @IdRes
    public val checkedId: Int,
    public val checked: Boolean
)
