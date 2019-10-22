package reactivecircus.flowbinding.android.widget

import android.widget.RatingBar
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of rating changes on the [RatingBar] instance
 * where the value emitted is the current rating.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
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
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RatingBar.ratingChanges(emitImmediately: Boolean = false): Flow<Float> = callbackFlow {
    checkMainThread()
    val listener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
        safeOffer(rating)
    }
    onRatingBarChangeListener = listener
    awaitClose { onRatingBarChangeListener = null }
}
    .startWithCurrentValue(emitImmediately) { rating }
    .conflate()
