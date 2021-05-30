package reactivecircus.flowbinding.android.widget

import android.widget.ListView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.ListFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AdapterViewItemClickEventFlowTest {

    @Test
    fun adapterViewItemClickEvents() {
        launchTest<ListFragment> {
            val recorder = FlowRecorder<AdapterViewItemClickEvent>(testScope)
            val listView = getViewById<ListView>(R.id.listView)

            listView.itemClickEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.performItemClick(listView.getChildAt(2), 2, 2)
            }
            val event = recorder.takeValue()
            assertThat(event.view)
                .isEqualTo(listView)
            assertThat(event.clickedView)
                .isNotNull()
            assertThat(event.position)
                .isEqualTo(2)
            assertThat(event.id)
                .isEqualTo(2)

            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                listView.performItemClick(listView.getChildAt(1), 1, 1)
            }
            recorder.assertNoMoreValues()
        }
    }
}
