package reactivecircus.flowbinding.activity

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.CheckResult
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of on back pressed events on the [OnBackPressedDispatcher] instance.
 *
 * @param owner the LifecycleOwner which controls when the callback should be invoked.
 *
 * Note: Created flow keeps a strong reference to the [OnBackPressedDispatcher] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * onBackPressedDispatcher.backPresses(lifecycleOwner)
 *     .onEach {
 *          // handle back pressed
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun OnBackPressedDispatcher.backPresses(owner: LifecycleOwner): Flow<Unit> =
    callbackFlow {
        checkMainThread()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                trySend(Unit)
            }
        }
        addCallback(owner, callback)
        awaitClose { callback.remove() }
    }.conflate()
