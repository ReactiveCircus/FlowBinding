package reactivecircus.flowbinding.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.swipeDownOnView
import reactivecircus.blueprint.testing.action.swipeLeftOnView
import reactivecircus.blueprint.testing.action.swipeRightOnView
import reactivecircus.blueprint.testing.action.swipeUpOnView
import reactivecircus.flowbinding.recyclerview.fixtures.RecyclerViewFragment
import reactivecircus.flowbinding.recyclerview.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class RecyclerViewScrollStateChangedFlowTest {

    @Test
    fun recyclerViewScrollStateChanges_vertical() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val recyclerView = getViewById<RecyclerView>(R.id.recyclerView).apply {
                runOnUiThread {
                    adapter = TestAdapter()
                }
            }
            getInstrumentation().waitForIdleSync()
            recyclerView.scrollStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeUpOnView(R.id.recyclerView)
            recorder.takeValue() shouldEqual RecyclerView.SCROLL_STATE_DRAGGING
            recorder.takeValue() shouldEqual RecyclerView.SCROLL_STATE_SETTLING
            recorder.assertNoMoreValues()

            cancelTestScope()
            recorder.clearValues()

            swipeDownOnView(R.id.recyclerView)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun recyclerViewScrollStateChanges_horizontal() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val recyclerView = getViewById<RecyclerView>(R.id.recyclerView).apply {
                runOnUiThread {
                    adapter = TestAdapter()
                    (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
                }
            }
            getInstrumentation().waitForIdleSync()
            recyclerView.scrollStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeLeftOnView(R.id.recyclerView)
            recorder.takeValue() shouldEqual RecyclerView.SCROLL_STATE_DRAGGING
            recorder.takeValue() shouldEqual RecyclerView.SCROLL_STATE_SETTLING
            recorder.takeValue() shouldEqual RecyclerView.SCROLL_STATE_IDLE
            recorder.assertNoMoreValues()

            cancelTestScope()
            recorder.clearValues()

            swipeRightOnView(R.id.recyclerView)
            recorder.assertNoMoreValues()
        }
    }
}
