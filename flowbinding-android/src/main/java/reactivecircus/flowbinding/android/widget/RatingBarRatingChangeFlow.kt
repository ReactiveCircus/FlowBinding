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
 * Create a [InitialValueFlow] of rating changes on the [RatingBar] instance
 * where the value emitted is the current rating.
 *
 * Note: Created flow keeps a strong reference to the [RatingBar] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * ratingBar.ratingChanges()
 *     .onEach { rating ->
 *          // handle rating
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun RatingBar.ratingChanges(): InitialValueFlow<Float> = callbackFlow {
    checkMainThread()
    val listener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
        trySend(rating)
    }
    onRatingBarChangeListener = listener
    awaitClose { onRatingBarChangeListener = null }
}
    .conflate()
    .asInitialValueFlow { rating }
