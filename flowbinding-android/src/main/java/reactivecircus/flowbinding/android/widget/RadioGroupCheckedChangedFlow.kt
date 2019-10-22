@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.widget

import android.widget.RadioButton
import android.widget.RadioGroup
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
 * Create a [Flow] of checked state change events on the [RadioButton] instance
 * where the value emitted is the currently checked radio button id.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [RadioButton] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * radioGroup.checkedChanges()
 *     .onEach { checkedId ->
 *          // handle checkedId
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RadioGroup.checkedChanges(emitImmediately: Boolean = false): Flow<Int> = callbackFlow<Int> {
    checkMainThread()
    val listener = object : RadioGroup.OnCheckedChangeListener {
        private var lastChecked = checkedRadioButtonId
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != lastChecked) {
                lastChecked = checkedId
                safeOffer(checkedId)
            }
        }
    }
    setOnCheckedChangeListener(listener)
    awaitClose { setOnCheckedChangeListener(null) }
}
    .startWithCurrentValue(emitImmediately) { checkedRadioButtonId }
    .conflate()
