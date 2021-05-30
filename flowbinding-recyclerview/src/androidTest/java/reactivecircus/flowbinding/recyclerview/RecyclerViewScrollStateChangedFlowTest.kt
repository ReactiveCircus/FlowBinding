package reactivecircus.flowbinding.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.common.truth.Truth.assertThat
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
                    adapter = TestAdapter(50)
                }
            }
            getInstrumentation().waitForIdleSync()
            recyclerView.scrollStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeUpOnView(R.id.recyclerView)
            assertThat(recorder.takeValue())
                .isEqualTo(RecyclerView.SCROLL_STATE_DRAGGING)
            assertThat(recorder.takeValue())
                .isEqualTo(RecyclerView.SCROLL_STATE_SETTLING)
            assertThat(recorder.takeValue())
                .isEqualTo(RecyclerView.SCROLL_STATE_IDLE)
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
                    adapter = TestAdapter(20)
                    (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
                }
            }
            getInstrumentation().waitForIdleSync()
            recyclerView.scrollStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeLeftOnView(R.id.recyclerView)
            assertThat(recorder.takeValue())
                .isEqualTo(RecyclerView.SCROLL_STATE_DRAGGING)
            assertThat(recorder.takeValue())
                .isEqualTo(RecyclerView.SCROLL_STATE_SETTLING)
            assertThat(recorder.takeValue())
                .isEqualTo(RecyclerView.SCROLL_STATE_IDLE)
            recorder.assertNoMoreValues()

            cancelTestScope()
            recorder.clearValues()

            swipeRightOnView(R.id.recyclerView)
            recorder.assertNoMoreValues()
        }
    }
}
