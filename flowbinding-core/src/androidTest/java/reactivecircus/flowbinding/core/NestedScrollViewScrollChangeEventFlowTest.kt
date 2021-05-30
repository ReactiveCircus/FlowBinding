package reactivecircus.flowbinding.core

import androidx.core.widget.NestedScrollView
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
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
            assertThat(event.view)
                .isEqualTo(nestedScrollView)
            assertThat(event.scrollX)
                .isEqualTo(100)
            assertThat(event.scrollY)
                .isEqualTo(0)
            assertThat(event.oldScrollX)
                .isEqualTo(0)
            assertThat(event.oldScrollY)
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            cancelTestScope()

            nestedScrollView.scrollTo(0, 0)
            recorder.assertNoMoreValues()
        }
    }
}
