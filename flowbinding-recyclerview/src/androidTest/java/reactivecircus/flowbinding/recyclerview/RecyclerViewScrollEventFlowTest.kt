package reactivecircus.flowbinding.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.recyclerview.fixtures.RecyclerViewFragment
import reactivecircus.flowbinding.recyclerview.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class RecyclerViewScrollEventFlowTest {

    @Test
    fun recyclerViewScrollEvents_vertical() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<RecyclerViewScrollEvent>(testScope)
            val recyclerView = getViewById<RecyclerView>(R.id.recyclerView).apply {
                runOnUiThread {
                    adapter = TestAdapter(50)
                }
            }
            getInstrumentation().waitForIdleSync()
            recyclerView.scrollEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                recyclerView.scrollBy(0, 50)
            }
            val event = recorder.takeValue()
            assertThat(event.view)
                .isEqualTo(recyclerView)
            assertThat(event.dx)
                .isEqualTo(0)
            assertThat(event.dy)
                .isEqualTo(50)

            cancelTestScope()
            recorder.clearValues()

            runOnUiThread {
                recyclerView.scrollBy(0, -50)
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun recyclerViewScrollEvents_horizontal() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<RecyclerViewScrollEvent>(testScope)
            val recyclerView = getViewById<RecyclerView>(R.id.recyclerView).apply {
                runOnUiThread {
                    adapter = TestAdapter(20)
                    (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
                }
            }
            getInstrumentation().waitForIdleSync()
            recyclerView.scrollEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                recyclerView.scrollBy(50, 0)
            }
            val event = recorder.takeValue()
            assertThat(event.view)
                .isEqualTo(recyclerView)
            assertThat(event.dx)
                .isEqualTo(50)
            assertThat(event.dy)
                .isEqualTo(0)

            cancelTestScope()
            recorder.clearValues()

            runOnUiThread {
                recyclerView.scrollBy(-50, 0)
            }
            recorder.assertNoMoreValues()
        }
    }
}
