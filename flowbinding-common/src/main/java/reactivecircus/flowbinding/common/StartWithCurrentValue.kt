package reactivecircus.flowbinding.common

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Create a flow that emits the value returned by [block] immediately if [emitImmediately] is true.
 * If [block] returns null, no value will be emitted immediately.
 */
@Deprecated(
    "Use type-safe asInitialValueFlow(initialValue) instead",
    replaceWith = ReplaceWith("this.asInitialValueFlow()")
)
@RestrictTo(LIBRARY_GROUP)
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.startWithCurrentValue(emitImmediately: Boolean, block: () -> T?): Flow<T> {
    return if (emitImmediately) onStart {
        block()?.run { emit(this) }
    } else this
}
