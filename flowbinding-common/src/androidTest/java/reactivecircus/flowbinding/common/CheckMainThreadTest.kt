package reactivecircus.flowbinding.common

import androidx.test.annotation.UiThreadTest
import androidx.test.filters.MediumTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.coInvoking
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
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
    fun checkMainThread_onMainThread() = runBlockingTest {
        mainThreadFlow.single() shouldEqual true
    }

    @Test
    fun checkMainThread_notMainThread() = runBlockingTest {
        coInvoking {
            mainThreadFlow.collect()
        } shouldThrow IllegalStateException::class
    }
}
