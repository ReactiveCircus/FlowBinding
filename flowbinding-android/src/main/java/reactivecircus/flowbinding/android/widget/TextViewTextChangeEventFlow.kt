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
 * Create a [Flow] of text change events on the [TextView] instance.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [TextView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * textView.textChangeEvents()
 *     .onEach { event ->
 *          // handle text view text change event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun TextView.textChangeEvents(emitImmediately: Boolean = false): Flow<TextChangeEvent> =
    callbackFlow<TextChangeEvent> {
        checkMainThread()
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                safeOffer(
                    TextChangeEvent(
                        view = this@textChangeEvents,
                        text = s,
                        start = start,
                        before = before,
                        count = count
                    )
                )
            }

            override fun afterTextChanged(s: Editable) = Unit
        }

        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }
        .startWithCurrentValue(emitImmediately) {
            TextChangeEvent(
                view = this@textChangeEvents,
                text = text,
                start = 0,
                before = 0,
                count = 0
            )
        }
        .conflate()

class TextChangeEvent(
    val view: TextView,
    val text: CharSequence,
    val start: Int,
    val before: Int,
    val count: Int
)
