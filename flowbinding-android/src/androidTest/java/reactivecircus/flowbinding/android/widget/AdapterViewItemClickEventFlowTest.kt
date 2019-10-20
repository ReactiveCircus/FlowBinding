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
class AdapterViewItemClickEventFlowTest {

    @Test
    fun adapterViewItemClickEvents() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<AdapterViewItemClickEvent>(testScope)
            val listView = getViewById<ListView>(R.id.listView)

            listView.itemClickEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                listView.performItemClick(listView.getChildAt(2), 2, 2)
            }
            val event = recorder.takeValue()
            event.view shouldEqual listView
            event.clickedView shouldNotBe null
            event.position shouldEqual 2
            event.id shouldEqual 2

            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                listView.performItemClick(listView.getChildAt(1), 1, 1)
            }
            recorder.assertNoMoreValues()
        }
    }
}
