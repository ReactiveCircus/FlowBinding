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
 * Create a [Flow] of icon clicked events on the [TextInputLayout] instance's start icon.
 *
 * Note: Created flow keeps a strong reference to the [TextInputLayout] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * textInputLayout.startIconClicks()
 *     .onEach {
 *          // handle start icon clicked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun TextInputLayout.startIconClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnClickListener {
        safeOffer(Unit)
    }
    setStartIconOnClickListener(listener)
    awaitClose { setStartIconOnClickListener(listener) }
}.conflate()

/**
 * Create a [Flow] of icon clicked events on the [TextInputLayout] instance's end icon.
 *
 * Note: Created flow keeps a strong reference to the [TextInputLayout] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * textInputLayout.endIconClicks()
 *     .onEach {
 *          // handle end icon clicked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun TextInputLayout.endIconClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnClickListener {
        safeOffer(Unit)
    }
    setEndIconOnClickListener(listener)
    awaitClose { setEndIconOnClickListener(listener) }
}.conflate()
