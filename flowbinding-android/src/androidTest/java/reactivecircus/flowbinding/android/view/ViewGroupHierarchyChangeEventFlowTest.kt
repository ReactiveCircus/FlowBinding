package reactivecircus.flowbinding.android.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
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
class ViewGroupHierarchyChangeEventFlowTest {

    private val appContext = ApplicationProvider.getApplicationContext<Context>().applicationContext

    @Test
    fun viewGroupHierarchyChangeEvents_childAdded() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<HierarchyChangeEvent.ChildAdded>(testScope)
            val viewGroup = getViewById<ViewGroup>(R.id.parentView)
            val childView1 = View(appContext)
            val childView2 = View(appContext)
            viewGroup.hierarchyChangeEvents()
                .filterIsInstance<HierarchyChangeEvent.ChildAdded>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { viewGroup.addView(childView1) }
            val event = recorder.takeValue()
            event.parent shouldEqual viewGroup
            event.child shouldEqual childView1
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { viewGroup.addView(childView2) }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun viewGroupHierarchyChangeEvents_childRemoved() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<HierarchyChangeEvent.ChildRemoved>(testScope)
            val childView1 = View(appContext)
            val childView2 = View(appContext)
            val viewGroup = getViewById<ViewGroup>(R.id.parentView).apply {
                runOnUiThread {
                    addView(childView1)
                    addView(childView2)
                }
            }
            viewGroup.hierarchyChangeEvents()
                .filterIsInstance<HierarchyChangeEvent.ChildRemoved>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { viewGroup.removeView(childView1) }
            val event = recorder.takeValue()
            event.parent shouldEqual viewGroup
            event.child shouldEqual childView1
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { viewGroup.removeView(childView2) }
            recorder.assertNoMoreValues()
        }
    }
}
