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
 * Create a [InitialValueFlow] of text changes on the [TextView] instance
 * where the value emitted is the current text.
 *
 * Note: Values emitted by this flow are **mutable** owned by the host [TextView]
 * and thus are **not safe** to cache or delay reading (such as by observing
 * on a different thread). If you want to cache or delay reading the items emitted then you must
 * map values through a function which calls [String.valueOf()] or
 * [CharSequence.toString()] to create a copy.
 *
 * Note: Created flow keeps a strong reference to the [TextView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * textView.textChanges()
 *     .onEach { text ->
 *          // handle text
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun TextView.textChanges(): InitialValueFlow<CharSequence> = callbackFlow<CharSequence> {
    checkMainThread()
    val listener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            safeOffer(s)
        }

        override fun afterTextChanged(s: Editable) = Unit
    }

    addTextChangedListener(listener)
    awaitClose { removeTextChangedListener(listener) }
}
    .conflate()
    .asInitialValueFlow { text }
