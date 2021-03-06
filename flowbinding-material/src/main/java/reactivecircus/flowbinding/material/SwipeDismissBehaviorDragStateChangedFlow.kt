package reactivecircus.flowbinding.material

import android.view.View
import androidx.annotation.CheckResult
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.behavior.SwipeDismissBehavior.STATE_DRAGGING
import com.google.android.material.behavior.SwipeDismissBehavior.STATE_IDLE
import com.google.android.material.behavior.SwipeDismissBehavior.STATE_SETTLING
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of drag state change events on the [View] instance with a [SwipeDismissBehavior]
 * where the value emitted can be one of [STATE_IDLE], [STATE_DRAGGING]} or [STATE_SETTLING].
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.dragStateChanges()
 *     .onEach { state ->
 *          // handle state
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun View.swipeDismissDragStateChanges(): Flow<Int> = callbackFlow {
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
        override fun onDismiss(view: View) = Unit

        override fun onDragStateChanged(state: Int) {
            trySend(state)
        }
    }
    behavior.listener = listener
    awaitClose { behavior.listener = null }
}.conflate()
