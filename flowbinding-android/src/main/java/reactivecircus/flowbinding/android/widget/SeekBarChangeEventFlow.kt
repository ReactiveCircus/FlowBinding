@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.widget

import android.widget.SeekBar
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [InitialValueFlow] of change events on the [SeekBar] instance.
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
@OptIn(ExperimentalCoroutinesApi::class)
public fun SeekBar.changeEvents(): InitialValueFlow<SeekBarChangeEvent> =
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
        .conflate()
        .asInitialValueFlow {
            SeekBarChangeEvent.ProgressChanged(
                view = this,
                progress = progress,
                fromUser = false
            )
        }

public sealed class SeekBarChangeEvent {
    public abstract val view: SeekBar

    public data class ProgressChanged(
        override val view: SeekBar,
        public val progress: Int,
        public val fromUser: Boolean
    ) : SeekBarChangeEvent()

    public data class StartTracking(override val view: SeekBar) : SeekBarChangeEvent()

    public data class StopTracking(override val view: SeekBar) : SeekBarChangeEvent()
}
