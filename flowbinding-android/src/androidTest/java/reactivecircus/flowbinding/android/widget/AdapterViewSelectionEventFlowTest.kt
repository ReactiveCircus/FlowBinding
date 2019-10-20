package reactivecircus.flowbinding.android.widget

import android.widget.ListView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AdapterViewSelectionEventFlowTest {

    @Test
    fun adapterViewSelectionEvents() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<AdapterViewSelectionEvent>(testScope)
            val listView = getViewById<ListView>(R.id.listView).apply {
                runOnUiThread { requestFocusFromTouch() }
            }
            listView.selectionEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.setSelection(2)
            }
            val event1 = recorder.takeValue() as AdapterViewSelectionEvent.ItemSelected
            event1.view shouldEqual listView
            event1.selectedView shouldNotBe null
            event1.position shouldEqual 2
            event1.id shouldEqual 2
            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.setSelection(0)
            }
            val event2 = recorder.takeValue() as AdapterViewSelectionEvent.ItemSelected
            event2.view shouldEqual listView
            event2.selectedView shouldNotBe null
            event2.position shouldEqual 0
            event2.id shouldEqual 0
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                listView.setSelection(1)
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun adapterViewSelectionEvents_emitImmediately() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<AdapterViewSelectionEvent>(testScope)
            val listView = getViewById<ListView>(R.id.listView).apply {
                runOnUiThread { requestFocusFromTouch() }
            }
            listView.selectionEvents(emitImmediately = true).recordWith(recorder)

            val event1 = recorder.takeValue() as AdapterViewSelectionEvent.ItemSelected
            event1.view shouldEqual listView
            event1.selectedView shouldNotBe null
            event1.position shouldEqual 0
            event1.id shouldEqual 0
            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.setSelection(2)
            }
            val event2 = recorder.takeValue() as AdapterViewSelectionEvent.ItemSelected
            event2.view shouldEqual listView
            event2.selectedView shouldNotBe null
            event2.position shouldEqual 2
            event2.id shouldEqual 2
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                listView.setSelection(1)
            }
            recorder.assertNoMoreValues()
        }
    }
}
