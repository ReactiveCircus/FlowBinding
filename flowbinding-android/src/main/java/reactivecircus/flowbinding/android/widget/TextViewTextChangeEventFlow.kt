@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
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
 * Create a [InitialValueFlow] of text change events on the [TextView] instance.
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
public fun TextView.textChangeEvents(): InitialValueFlow<TextChangeEvent> =
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
        .conflate()
        .asInitialValueFlow {
            TextChangeEvent(
                view = this@textChangeEvents,
                text = text,
                start = 0,
                before = 0,
                count = 0
            )
        }

public data class TextChangeEvent(
    public val view: TextView,
    public val text: CharSequence,
    public val start: Int,
    public val before: Int,
    public val count: Int
)
