package reactivecircus.flowbinding.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.kluentsuspend.invokingSuspend
import reactivecircus.kluentsuspend.shouldThrow

@ExperimentalCoroutinesApi
class StartWithCurrentValueTest {

    @Test
    fun `invoke and emit value from block on flow collection when emitImmediately is true and block returns value`() =
        runBlockingTest {
            var invokedBlock = false
            val result = flowOf<Int>()
                .startWithCurrentValue(emitImmediately = true, block = {
                    invokedBlock = true
                    0
                })
                .single()

            invokedBlock shouldEqual true
            result shouldEqual 0
        }

    @Test
    fun `does not invoke and emit value from block on flow collection when emitImmediately is false`() =
        runBlockingTest {
            var invokedBlock = false
            invokingSuspend {
                flowOf<Int>().startWithCurrentValue(emitImmediately = false, block = {
                    invokedBlock = true
                    0
                }).single()
            } shouldThrow NoSuchElementException::class

            invokedBlock shouldEqual false
        }

    @Test
    fun `invoke block but does not emit value on flow collection when emitImmediately is true but block returns null`() =
        runBlockingTest {
            var invokedBlock = false
            invokingSuspend {
                flowOf<Int>()
                    .startWithCurrentValue(emitImmediately = true, block = {
                        invokedBlock = true
                        null
                    }).single()
            } shouldThrow NoSuchElementException::class

            invokedBlock shouldEqual true
        }
}
