@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.preference

import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.preference.EditTextPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of bind events on the [EditTextPreference] instance.
 *
 * Note: Created flow keeps a strong reference to the [EditTextPreference] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * editTextPreference.editTextBindEvents()
 *     .onEach { event ->
 *          // handle edit text preference bind event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun EditTextPreference.editTextBindEvents(): Flow<EditTextBindEvent> = callbackFlow {
    checkMainThread()
    val listener = EditTextPreference.OnBindEditTextListener {
        safeOffer(EditTextBindEvent(it))
    }
    setOnBindEditTextListener(listener)
    awaitClose { setOnBindEditTextListener(null) }
}.conflate()

class EditTextBindEvent(val editText: EditText)
