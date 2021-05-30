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
class AdapterViewSelectionEventFlowTest {

    @Test
    fun adapterViewSelectionEvents() {
        launchTest<ListFragment> {
            val recorder = FlowRecorder<AdapterViewSelectionEvent>(testScope)
            val listView = getViewById<ListView>(R.id.listView).apply {
                runOnUiThread { requestFocusFromTouch() }
            }
            listView.selectionEvents().recordWith(recorder)

            val event1 = recorder.takeValue() as AdapterViewSelectionEvent.ItemSelected
            assertThat(event1.view)
                .isEqualTo(listView)
            assertThat(event1.selectedView)
                .isNotNull()
            assertThat(event1.position)
                .isEqualTo(0)
            assertThat(event1.id)
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.setSelection(2)
            }
            val event2 = recorder.takeValue() as AdapterViewSelectionEvent.ItemSelected
            assertThat(event2.view)
                .isEqualTo(listView)
            assertThat(event2.selectedView)
                .isNotNull()
            assertThat(event2.position)
                .isEqualTo(2)
            assertThat(event2.id)
                .isEqualTo(2)
            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.setSelection(0)
            }
            val event3 = recorder.takeValue() as AdapterViewSelectionEvent.ItemSelected
            assertThat(event3.view)
                .isEqualTo(listView)
            assertThat(event3.selectedView)
                .isNotNull()
            assertThat(event3.position)
                .isEqualTo(0)
            assertThat(event3.id)
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                listView.setSelection(1)
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun adapterViewSelectionEvents_skipInitialValue() {
        launchTest<ListFragment> {
            val recorder = FlowRecorder<AdapterViewSelectionEvent>(testScope)
            val listView = getViewById<ListView>(R.id.listView).apply {
                runOnUiThread { requestFocusFromTouch() }
            }
            listView.selectionEvents()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.setSelection(2)
            }
            val event = recorder.takeValue() as AdapterViewSelectionEvent.ItemSelected
            assertThat(event.view)
                .isEqualTo(listView)
            assertThat(event.selectedView)
                .isNotNull()
            assertThat(event.position)
                .isEqualTo(2)
            assertThat(event.id)
                .isEqualTo(2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                listView.setSelection(1)
            }
            recorder.assertNoMoreValues()
        }
    }
}
