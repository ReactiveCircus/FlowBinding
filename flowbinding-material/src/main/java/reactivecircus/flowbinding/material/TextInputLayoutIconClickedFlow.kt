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

/**
 * Create a [Flow] of clicked events on the [TextInputLayout] instance's start icon.
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
@OptIn(ExperimentalCoroutinesApi::class)
public fun TextInputLayout.startIconClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnClickListener {
        trySend(Unit)
    }
    setStartIconOnClickListener(listener)
    awaitClose { setStartIconOnClickListener(null) }
}.conflate()

/**
 * Create a [Flow] of clicked events on the [TextInputLayout] instance's end icon.
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
@OptIn(ExperimentalCoroutinesApi::class)
public fun TextInputLayout.endIconClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnClickListener {
        trySend(Unit)
    }
    setEndIconOnClickListener(listener)
    awaitClose { setEndIconOnClickListener(null) }
}.conflate()

/**
 * Create a [Flow] of clicked events on the [TextInputLayout] instance's error icon.
 *
 * Note: Created flow keeps a strong reference to the [TextInputLayout] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * textInputLayout.errorIconClicks()
 *     .onEach {
 *          // handle error icon clicked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun TextInputLayout.errorIconClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnClickListener {
        trySend(Unit)
    }
    setErrorIconOnClickListener(listener)
    awaitClose { setErrorIconOnClickListener(null) }
}.conflate()
