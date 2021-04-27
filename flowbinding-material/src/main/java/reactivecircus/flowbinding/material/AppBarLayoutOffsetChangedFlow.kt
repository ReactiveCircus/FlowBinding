package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of offset changed events on the [AppBarLayout] instance
 * where the value emitted is the verticalOffset of the [AppBarLayout].
 *
 * Note: Created flow keeps a strong reference to the [AppBarLayout] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * snackbar.offsetChanges()
 *     .onEach { verticalOffset ->
 *          // handle verticalOffset
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun AppBarLayout.offsetChanges(): Flow<Int> = callbackFlow {
    checkMainThread()
    val listener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        trySend(verticalOffset)
    }
    addOnOffsetChangedListener(listener)
    awaitClose { removeOnOffsetChangedListener(listener) }
}.conflate()
