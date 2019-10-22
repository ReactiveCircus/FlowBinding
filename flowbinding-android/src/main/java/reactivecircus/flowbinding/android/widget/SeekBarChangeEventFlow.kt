@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.widget

import android.widget.SeekBar
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
 * Create a [Flow] of change events on the [SeekBar] instance.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [SeekBar] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * seekBar.changeEvents()
 *     .onEach { event ->
 *          when(event) {
 *              SeekBarChangeEvent.ProgressChanged -> {
 *                  // handle seek bar progress changed event
 *              }
 *              SeekBarChangeEvent.StartTracking -> {
 *                  // handle seek bar start tracking event
 *              }
 *              SeekBarChangeEvent.StopTracking -> {
 *                  // handle seek bar stop tracking event
 *              }
 *          }
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun SeekBar.changeEvents(emitImmediately: Boolean = false): Flow<SeekBarChangeEvent> =
    callbackFlow<SeekBarChangeEvent> {
        checkMainThread()
        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                safeOffer(
                    SeekBarChangeEvent.ProgressChanged(
                        view = seekBar,
                        progress = progress,
                        fromUser = fromUser
                    )
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                safeOffer(
                    SeekBarChangeEvent.StartTracking(view = seekBar)
                )
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                safeOffer(
                    SeekBarChangeEvent.StopTracking(view = seekBar)
                )
            }
        }

        setOnSeekBarChangeListener(listener)
        awaitClose { setOnSeekBarChangeListener(null) }
    }
        .startWithCurrentValue(emitImmediately) {
            SeekBarChangeEvent.ProgressChanged(
                view = this,
                progress = progress,
                fromUser = false
            )
        }
        .conflate()

sealed class SeekBarChangeEvent {
    abstract val view: SeekBar

    class ProgressChanged(
        override val view: SeekBar,
        val progress: Int,
        val fromUser: Boolean
    ) : SeekBarChangeEvent()

    class StartTracking(override val view: SeekBar) : SeekBarChangeEvent()

    class StopTracking(override val view: SeekBar) : SeekBarChangeEvent()
}
