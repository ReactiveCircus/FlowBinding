package reactivecircus.flowbinding.android.view

import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.CheckResult
import androidx.annotation.RequiresApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of draw events on the [View] instance.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.draws()
 *     .onEach {
 *          // handle draw
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun View.draws(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = ViewTreeObserver.OnDrawListener {
        trySend(Unit)
    }
    viewTreeObserver.addOnDrawListener(listener)
    awaitClose { viewTreeObserver.removeOnDrawListener(listener) }
}.conflate()
