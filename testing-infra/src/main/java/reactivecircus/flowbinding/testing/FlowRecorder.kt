package reactivecircus.flowbinding.testing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit

@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.recordWith(recorder: FlowRecorder<T>) {
    onEach { recorder.values.addLast(it) }.launchIn(recorder.coroutineScope)
}

class FlowRecorder<T>(internal val coroutineScope: CoroutineScope) {

    internal val values: BlockingDeque<T> = LinkedBlockingDeque<T>()

    fun takeValue(): T {
        return values.pollFirst(1, TimeUnit.SECONDS)
            ?: throw NoSuchElementException("No value found.")
    }

    fun clearValues() {
        values.clear()
    }

    @Suppress("UseCheckOrError")
    fun assertNoMoreValues() {
        try {
            val value = takeValue()
            throw IllegalStateException("Expected no more values but got $value")
        } catch (ignored: NoSuchElementException) {
        }
    }
}
