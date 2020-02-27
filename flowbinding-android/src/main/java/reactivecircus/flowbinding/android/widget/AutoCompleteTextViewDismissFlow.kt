package reactivecircus.flowbinding.android.widget

import android.os.Build
import android.widget.AutoCompleteTextView
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
 * Create a [Flow] of dismiss events on the [AutoCompleteTextView] instance.
 *
 * Note: Created flow keeps a strong reference to the [AutoCompleteTextView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * autoCompleteTextView.dismisses()
 *     .onEach {
 *          // handle auto-complete text view dismiss event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun AutoCompleteTextView.dismisses(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = AutoCompleteTextView.OnDismissListener {
        safeOffer(Unit)
    }
    setOnDismissListener(listener)
    awaitClose { setOnDismissListener(null) }
}.conflate()
