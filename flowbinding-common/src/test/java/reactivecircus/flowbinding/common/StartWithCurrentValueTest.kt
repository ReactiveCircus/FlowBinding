package reactivecircus.flowbinding.common

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class StartWithCurrentValueTest {

    @Test
    fun `invoke and emit value from block on flow collection when emitImmediately is true`() =
        runBlockingTest {
            val result = flowOf<Int>()
                .startWithCurrentValue(emitImmediately = true, block = { 0 })
                .single()

            assertThat(result).isEqualTo(0)
        }

    @Test(expected = NoSuchElementException::class)
    fun `does not invoke and emit value from block on flow collection when emitImmediately is false`() =
        runBlockingTest {
            flowOf<Int>().startWithCurrentValue(emitImmediately = false, block = { 0 }).single()
        }
}
