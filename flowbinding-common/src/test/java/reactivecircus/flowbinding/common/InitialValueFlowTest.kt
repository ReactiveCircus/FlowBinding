package reactivecircus.flowbinding.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldEqual
import org.junit.Test

@ExperimentalCoroutinesApi
class InitialValueFlowTest {

    @Test
    fun `initial value is emitted by an InitialValueFlow`() = runBlockingTest {
        val result = emptyFlow<Int>().asInitialValueFlow { 1 }.single()
        result shouldEqual 1
    }

    @Test
    fun `initial value is not emitted by an InitialValueFlow when initialValue lambda returns null`() =
        runBlockingTest {
            val result = emptyFlow<Int>().asInitialValueFlow { null }.singleOrNull()
            result shouldEqual null
        }

    @Test
    fun `initial value is not emitted by a flow returned by skipInitialValue()`() = runBlockingTest {
        val result = emptyFlow<Int>().asInitialValueFlow { 1 }.skipInitialValue().singleOrNull()
        result shouldEqual null
    }
}
