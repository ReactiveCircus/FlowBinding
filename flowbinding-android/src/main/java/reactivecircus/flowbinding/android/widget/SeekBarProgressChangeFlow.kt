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
 * Create a [InitialValueFlow] of progress changes on the [SeekBar] instance
 * where the value emitted is the current progress.
 *
 * Note: Created flow keeps a strong reference to the [SeekBar] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * seekBar.progressChanges()
 *     .onEach { progress ->
 *          // handle progress
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun SeekBar.progressChanges(): InitialValueFlow<Int> = callbackFlow<Int> {
    checkMainThread()
    val listener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            safeOffer(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

        override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
    }

    setOnSeekBarChangeListener(listener)
    awaitClose { setOnSeekBarChangeListener(null) }
}
    .conflate()
    .asInitialValueFlow { progress }
