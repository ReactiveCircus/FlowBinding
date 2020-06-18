package reactivecircus.flowbinding.common

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.onStart

/**
 * Converts a [Flow] to an [InitialValueFlow], taking an [initialValue] lambda for computing the initial value.
 */
@RestrictTo(LIBRARY_GROUP)
@OptIn(ExperimentalCoroutinesApi::class)
fun <T : Any> Flow<T>.asInitialValueFlow(initialValue: () -> T): InitialValueFlow<T> = InitialValueFlow(
    onStart { emit(initialValue()) }
)

/**
 * A [Flow] implementation that emits the current value of a widget immediately upon collection.
 */
class InitialValueFlow<T : Any>(private val flow: Flow<T>) : Flow<T> by flow {

    /**
     * Returns a [Flow] that skips the initial emission of the current value.
     */
    fun skipInitialValue(): Flow<T> = flow.drop(1)
}
