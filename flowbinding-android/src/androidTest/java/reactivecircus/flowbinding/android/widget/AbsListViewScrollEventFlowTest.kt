package reactivecircus.flowbinding.android.widget

import android.widget.AbsListView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.junit.Test
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.android.fixtures.widget.AbsListFragment
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
            event1.view shouldEqual absListView
            event1.scrollState shouldEqual AbsListView.OnScrollListener.SCROLL_STATE_IDLE
            event1.firstVisibleItem shouldEqual 0
            event1.visibleItemCount shouldBeGreaterThan 0
            event1.totalItemCount shouldEqual 50

            runOnUiThread {
                absListView.scrollListBy(1000)
            }
            val event2 = recorder.takeValue()
            event2.view shouldEqual absListView
            event2.scrollState shouldEqual AbsListView.OnScrollListener.SCROLL_STATE_IDLE
            event2.firstVisibleItem shouldNotEqual 0
            event2.visibleItemCount shouldBeGreaterThan 0
            event2.totalItemCount shouldEqual 50

            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                absListView.smoothScrollToPosition(0)
            }
            recorder.assertNoMoreValues()
        }
    }
}
