package reactivecircus.flowbinding.common

import androidx.test.annotation.UiThreadTest
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test

@MediumTest
@ExperimentalCoroutinesApi
class CheckMainThreadTest {

    private val mainThreadFlow = flow {
        checkMainThread()
        emit(true)
    }

    @Test
    @UiThreadTest
    fun checkMainThread_onMainThread() = runTest {
        assertThat(mainThreadFlow.single())
            .isTrue()
    }

    @Test
    fun checkMainThread_notMainThread() = runTest {
        assertThrows<IllegalStateException> { mainThreadFlow.collect() }
    }
}
