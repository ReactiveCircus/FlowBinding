package reactivecircus.flowbinding.material

import android.view.View
import androidx.annotation.CheckResult
import com.google.android.material.chip.Chip
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of close icon click events on the [Chip] instance.
 *
 * Note: Created flow keeps a strong reference to the [Chip] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * chip.closeIconClicks()
 *     .onEach {
 *          // handle close icon clicked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun Chip.closeIconClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnClickListener {
        safeOffer(Unit)
    }
    setOnCloseIconClickListener(listener)
    awaitClose { setOnCloseIconClickListener(null) }
}.conflate()
