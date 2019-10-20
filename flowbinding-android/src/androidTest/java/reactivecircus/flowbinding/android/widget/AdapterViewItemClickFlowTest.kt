package reactivecircus.flowbinding.android.widget

import android.widget.ListView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AdapterViewItemClickFlowTest {

    @Test
    fun adapterViewItemClicks() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val listView = getViewById<ListView>(R.id.listView)

            listView.itemClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.performItemClick(listView.getChildAt(2), 2, 2)
            }
            recorder.takeValue() shouldEqual 2

            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                listView.performItemClick(listView.getChildAt(1), 1, 1)
            }
            recorder.assertNoMoreValues()
        }
    }
}
