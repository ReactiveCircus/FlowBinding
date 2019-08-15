package reactivecircus.flowbinding.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Create a flow that emits the value returned by [block] immediately if [emitImmediately] is true.
 */
@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.startWithCurrentValue(emitImmediately: Boolean, block: () -> T): Flow<T> {
    return if (emitImmediately) onStart { emit(block()) } else this
}
