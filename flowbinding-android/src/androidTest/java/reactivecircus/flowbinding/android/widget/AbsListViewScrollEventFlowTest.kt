package reactivecircus.flowbinding.android.widget

import android.widget.AbsListView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AbsListFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AbsListViewScrollEventFlowTest {

    @Test
    fun absListViewScrollEvents() {
        launchTest<AbsListFragment> {
            val recorder = FlowRecorder<ScrollEvent>(testScope)
            val absListView = getViewById<AbsListView>(R.id.absListView)

            absListView.scrollEvents().recordWith(recorder)

            val event1 = recorder.takeValue()
            assertThat(event1.view)
                .isEqualTo(absListView)
            assertThat(event1.scrollState)
                .isEqualTo(AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
            assertThat(event1.firstVisibleItem)
                .isEqualTo(0)
            assertThat(event1.visibleItemCount)
                .isGreaterThan(0)
            assertThat(event1.totalItemCount)
                .isEqualTo(50)

            runOnUiThread {
                absListView.scrollListBy(1000)
            }
            val event2 = recorder.takeValue()
            assertThat(event2.view)
                .isEqualTo(absListView)
            assertThat(event2.scrollState)
                .isEqualTo(AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
            assertThat(event2.firstVisibleItem)
                .isNotEqualTo(0)
            assertThat(event2.visibleItemCount)
                .isGreaterThan(0)
            assertThat(event2.totalItemCount)
                .isEqualTo(50)

            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                absListView.smoothScrollToPosition(0)
            }
            recorder.assertNoMoreValues()
        }
    }
}
