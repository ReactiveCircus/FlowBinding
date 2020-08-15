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
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of global layout events on the [View] instance.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.globalLayouts()
 *     .onEach {
 *          // handle global layout
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun View.globalLayouts(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = ViewTreeObserver.OnGlobalLayoutListener {
        safeOffer(Unit)
    }
    viewTreeObserver.addOnGlobalLayoutListener(listener)
    awaitClose { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
}.conflate()
