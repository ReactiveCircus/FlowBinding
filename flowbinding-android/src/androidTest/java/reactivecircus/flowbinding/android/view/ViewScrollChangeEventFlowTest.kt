package reactivecircus.flowbinding.android.view

import android.os.Build
import android.view.View
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewScrollChangeEventFlowTest {

    @Before
    fun setUp() {
        assumeTrue(
            "Requires API level 23.",
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        )
    }

    @Test
    fun viewScrollChangeEvents() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<ScrollChangeEvent>(testScope)
            val view = View(getRootView().context)

            view.scrollChangeEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            view.scrollTo(10, 10)
            val event = recorder.takeValue()
            event.view shouldEqual view
            event.scrollX shouldEqual 10
            event.scrollY shouldEqual 10
            event.oldScrollX shouldEqual 0
            event.oldScrollY shouldEqual 0
            recorder.assertNoMoreValues()

            cancelTestScope()

            view.scrollTo(0, 0)
            recorder.assertNoMoreValues()
        }
    }
}
