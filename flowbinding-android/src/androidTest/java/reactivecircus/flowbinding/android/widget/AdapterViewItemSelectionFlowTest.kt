package reactivecircus.flowbinding.android.widget

import android.widget.ListView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.ListFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AdapterViewItemSelectionFlowTest {

    @Test
    fun adapterViewItemSelections() {
        launchTest<ListFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val listView = getViewById<ListView>(R.id.listView).apply {
                runOnUiThread { requestFocusFromTouch() }
            }
            listView.itemSelections().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.setSelection(2)
            }
            recorder.takeValue() shouldEqual 2
            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.setSelection(0)
            }
            recorder.takeValue() shouldEqual 0
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                listView.setSelection(1)
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun adapterViewItemSelections_emitImmediately() {
        launchTest<ListFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val listView = getViewById<ListView>(R.id.listView).apply {
                runOnUiThread { requestFocusFromTouch() }
            }
            listView.itemSelections(emitImmediately = true).recordWith(recorder)

            recorder.takeValue() shouldEqual 0
            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.setSelection(2)
            }
            recorder.takeValue() shouldEqual 2
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                listView.setSelection(1)
            }
            recorder.assertNoMoreValues()
        }
    }
}
