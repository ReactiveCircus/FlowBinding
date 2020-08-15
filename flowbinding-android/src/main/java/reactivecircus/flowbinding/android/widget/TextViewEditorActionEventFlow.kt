@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.widget

import android.view.KeyEvent
import android.widget.TextView
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of editor action events on the [TextView] instance.
 *
 * @param handled function to be invoked with each value to determine the return value of
 * the underlying [TextView.OnEditorActionListener]. Note that the [Flow] will only emit when this function
 * evaluates to true.
 *
 * Note: Created flow keeps a strong reference to the [TextView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * textView.editorActionEvents { event.actionId == EditorInfo.IME_ACTION_GO }
 *     .onEach { event ->
 *          // handle text view editor action event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun TextView.editorActionEvents(handled: (EditorActionEvent) -> Boolean = { true }): Flow<EditorActionEvent> =
    callbackFlow {
        checkMainThread()
        val listener = TextView.OnEditorActionListener { v, actionId, keyEvent ->
            val event = EditorActionEvent(
                view = v,
                actionId = actionId,
                keyEvent = keyEvent
            )
            if (handled(event)) {
                safeOffer(event)
                true
            } else {
                false
            }
        }
        setOnEditorActionListener(listener)
        awaitClose { setOnEditorActionListener(null) }
    }.conflate()

public class EditorActionEvent(
    public val view: TextView,
    public val actionId: Int,
    public val keyEvent: KeyEvent?
)
