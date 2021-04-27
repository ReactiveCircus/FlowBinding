@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.widget

import android.widget.RatingBar
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [InitialValueFlow] of rating change events on the [RatingBar] instance.
 *
 * Note: Created flow keeps a strong reference to the [RatingBar] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * ratingBar.ratingChangeEvents()
 *     .onEach { event ->
 *          // handle rating change event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun RatingBar.ratingChangeEvents(): InitialValueFlow<RatingChangeEvent> = callbackFlow {
    checkMainThread()
    val listener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
        trySend(
            RatingChangeEvent(
                view = ratingBar,
                rating = rating,
                fromUser = fromUser
            )
        )
    }
    onRatingBarChangeListener = listener
    awaitClose { onRatingBarChangeListener = null }
}
    .conflate()
    .asInitialValueFlow {
        RatingChangeEvent(
            view = this,
            rating = rating,
            fromUser = false
        )
    }

public data class RatingChangeEvent(
    public val view: RatingBar,
    public val rating: Float,
    public val fromUser: Boolean
)
