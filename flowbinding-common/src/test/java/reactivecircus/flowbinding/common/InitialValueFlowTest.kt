package reactivecircus.flowbinding.common

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class InitialValueFlowTest {

    @Test
    fun `initial value is emitted by an InitialValueFlow`() = runTest {
        val result = emptyFlow<Int>().asInitialValueFlow { 1 }.single()
        assertThat(result)
            .isEqualTo(1)
    }

    @Test
    fun `initial value is not emitted by a flow returned by skipInitialValue()`() = runTest {
        val result = emptyFlow<Int>().asInitialValueFlow { 1 }.skipInitialValue().singleOrNull()
        assertThat(result)
            .isNull()
    }
}
