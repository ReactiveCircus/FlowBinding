package reactivecircus.flowbinding.material

import android.view.View
import androidx.annotation.CheckResult
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of icon long clicked events on the [TextInputLayout] instance's start icon.
 *
 * Note: Created flow keeps a strong reference to the [TextInputLayout] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * textInputLayout.startIconLongClicks()
 *     .onEach {
 *          // handle start icon long clicked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun TextInputLayout.startIconLongClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnLongClickListener {
        safeOffer(Unit)
    }
    setStartIconOnLongClickListener(listener)
    awaitClose { setStartIconOnLongClickListener(null) }
}.conflate()

/**
 * Create a [Flow] of icon long clicked events on the [TextInputLayout] instance's end icon.
 *
 * Note: Created flow keeps a strong reference to the [TextInputLayout] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * textInputLayout.endIconLongClicks()
 *     .onEach {
 *          // handle end icon long clicked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun TextInputLayout.endIconLongClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnLongClickListener {
        safeOffer(Unit)
    }
    setEndIconOnLongClickListener(listener)
    awaitClose { setEndIconOnLongClickListener(null) }
}.conflate()
