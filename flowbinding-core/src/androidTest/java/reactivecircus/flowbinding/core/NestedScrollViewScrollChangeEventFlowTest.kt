package reactivecircus.flowbinding.core

import androidx.core.widget.NestedScrollView
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.core.fixtures.CoreFragment
import reactivecircus.flowbinding.core.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class NestedScrollViewScrollChangeEventFlowTest {

    @Test
    fun nestedScrollViewScrollChangeEvents() {
        launchTest<CoreFragment> {
            val recorder = FlowRecorder<ScrollChangeEvent>(testScope)
            val nestedScrollView = getViewById<NestedScrollView>(R.id.nestedScrollView)

            nestedScrollView.scrollChangeEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            nestedScrollView.scrollTo(100, 0)
            val event = recorder.takeValue()
            event.view shouldEqual nestedScrollView
            event.scrollX shouldEqual 100
            event.scrollY shouldEqual 0
            event.oldScrollX shouldEqual 0
            event.oldScrollY shouldEqual 0
            recorder.assertNoMoreValues()

            cancelTestScope()

            nestedScrollView.scrollTo(0, 0)
            recorder.assertNoMoreValues()
        }
    }
}
