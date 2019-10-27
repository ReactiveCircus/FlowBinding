package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of positive button click events on the [MaterialDatePicker] instance
 * when the user confirms a valid selection,
 * where the value emitted is the selection made.
 *
 * Note: Created flow keeps a strong reference to the [MaterialDatePicker] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * datePicker.positiveButtonClicks()
 *     .onEach { selection ->
 *          // handle selection (as a result of positive button click)
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun <S> MaterialDatePicker<S>.positiveButtonClicks(): Flow<S> = callbackFlow<S> {
    checkMainThread()
    val listener = MaterialPickerOnPositiveButtonClickListener<S> {
        safeOffer(it)
    }
    addOnPositiveButtonClickListener(listener)
    awaitClose { removeOnPositiveButtonClickListener(listener) }
}.conflate()
