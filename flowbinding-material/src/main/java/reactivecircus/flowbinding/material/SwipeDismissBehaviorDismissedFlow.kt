package reactivecircus.flowbinding.material

import android.view.View
import androidx.annotation.CheckResult
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.SwipeDismissBehavior
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of dismissed events on the [View] instance with a [SwipeDismissBehavior]
 * where the value emitted is the view dismissed.
 * The [View]'s layoutParams must be of the type [CoordinatorLayout.LayoutParams],
 * and the layoutParams's behavior must be a [SwipeDismissBehavior].
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.dismisses()
 *     .onEach { dismissedView ->
 *          // handle dismissedView
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun View.dismisses(): Flow<View> = callbackFlow<View> {
    checkMainThread()
    val params = layoutParams
    check(params is CoordinatorLayout.LayoutParams) {
        "View is not in a CoordinatorLayout."
    }
    val behavior = params.behavior
    check(behavior is SwipeDismissBehavior) {
        "Behavior is not a SwipeDismissBehavior."
    }
    val listener = object : SwipeDismissBehavior.OnDismissListener {
        override fun onDismiss(view: View) {
            safeOffer(view)
        }

        override fun onDragStateChanged(state: Int) = Unit
    }
    behavior.setListener(listener)
    awaitClose { behavior.setListener(null) }
}.conflate()
