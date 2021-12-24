package reactivecircus.flowbinding.common

import androidx.test.annotation.UiThreadTest
import androidx.test.filters.MediumTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertThrows
import org.junit.Test

@MediumTest
@ExperimentalCoroutinesApi
class CheckMainThreadTest {

    @Test
    @UiThreadTest
    fun checkMainThread_onMainThread() {
        checkMainThread()
    }

    @Test
    fun checkMainThread_notMainThread() {
        assertThrows(IllegalStateException::class.java) {
            checkMainThread()
        }
    }
}
