package reactivecircus.flowbinding.android.view

import android.view.View
import android.view.ViewGroup
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import kotlinx.coroutines.flow.filterIsInstance
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewAttachEventFlowTest {

    @Test
    fun viewAttachEvents_attached() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<ViewAttachEvent.Attached>(testScope)
            val viewGroup = getViewById<ViewGroup>(R.id.parentView)
            val childView1 = View(rootView.context)
            val childView2 = View(rootView.context)
            childView1.attachEvents()
                .filterIsInstance<ViewAttachEvent.Attached>()
                .recordWith(recorder)
            childView2.attachEvents()
                .filterIsInstance<ViewAttachEvent.Attached>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { viewGroup.addView(childView1) }
            recorder.takeValue().view shouldEqual childView1
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { viewGroup.addView(childView2) }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun viewAttachEvents_detached() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<ViewAttachEvent.Detached>(testScope)
            val childView1 = View(rootView.context)
            val childView2 = View(rootView.context)
            val viewGroup = getViewById<ViewGroup>(R.id.parentView).apply {
                runOnUiThread {
                    addView(childView1)
                    addView(childView2)
                }
            }
            childView1.attachEvents()
                .filterIsInstance<ViewAttachEvent.Detached>()
                .recordWith(recorder)
            childView2.attachEvents()
                .filterIsInstance<ViewAttachEvent.Detached>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { viewGroup.removeView(childView1) }
            recorder.takeValue().view shouldEqual childView1
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { viewGroup.removeView(childView2) }
            recorder.assertNoMoreValues()
        }
    }
}
