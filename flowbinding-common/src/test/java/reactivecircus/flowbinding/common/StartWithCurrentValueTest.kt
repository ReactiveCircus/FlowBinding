package reactivecircus.flowbinding.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.flowassert.test

@ExperimentalCoroutinesApi
class StartWithCurrentValueTest {

    @Test
    fun `invoke and emit value from block on flow collection when emitImmediately is true`() =
        runBlockingTest {
            flowOf<Int>().startWithCurrentValue(emitImmediately = true, block = { 0 }).test {
                expectItem() shouldEqual 0
                expectComplete()
                expectNoMoreEvents()
            }
        }

    @Test
    fun `does not invoke and emit value from block on flow collection when emitImmediately is false`() =
        runBlockingTest {
            flowOf<Int>().startWithCurrentValue(emitImmediately = false, block = { 0 }).test {
                expectComplete()
                expectNoMoreEvents()
            }
        }
}
