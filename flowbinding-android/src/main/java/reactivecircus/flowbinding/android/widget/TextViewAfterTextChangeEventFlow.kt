@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
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
 * Create a [Flow] of after text change events on the [TextView] instance.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [TextView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * textView.afterTextChanges()
 *     .onEach { event ->
 *          // handle text view after text change event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun TextView.afterTextChanges(emitImmediately: Boolean = false): Flow<AfterTextChangeEvent> =
    callbackFlow<AfterTextChangeEvent> {
        checkMainThread()
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable) {
                safeOffer(AfterTextChangeEvent(view = this@afterTextChanges, editable = s))
            }
        }

        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }
        .startWithCurrentValue(emitImmediately) {
            AfterTextChangeEvent(view = this@afterTextChanges, editable = editableText)
        }
        .conflate()

class AfterTextChangeEvent(
    val view: TextView,
    val editable: Editable?
)
