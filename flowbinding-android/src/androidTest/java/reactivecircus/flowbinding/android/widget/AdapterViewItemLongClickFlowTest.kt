package reactivecircus.flowbinding.android.widget

import android.widget.ListView
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.longClickAdapterViewItemAt
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AdapterViewItemLongClickFlowTest {

    @Test
    fun adapterViewItemLongClicks() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val listView = getViewById<ListView>(R.id.listView)

            listView.itemLongClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            longClickAdapterViewItemAt(R.id.listView, 2)
            recorder.takeValue() shouldEqual 2

            recorder.assertNoMoreValues()

            cancelTestScope()

            longClickAdapterViewItemAt(R.id.listView, 1)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun adapterViewItemLongClicks_notHandled() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val listView = getViewById<ListView>(R.id.listView)

            listView.itemLongClicks { false }.recordWith(recorder)

            recorder.assertNoMoreValues()

            longClickAdapterViewItemAt(R.id.listView, 2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            longClickAdapterViewItemAt(R.id.listView, 1)
            recorder.assertNoMoreValues()
        }
    }
}
