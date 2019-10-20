package reactivecircus.flowbinding.android.widget

import android.widget.Adapter
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.android.testutil.TestAdapter
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AdapterDataChangeFlowTest {

    @Test
    fun adapterDataChanges() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Adapter>(testScope)
            val adapter = TestAdapter()

            adapter.dataChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            adapter.notifyDataSetChanged()
            recorder.takeValue() shouldEqual adapter

            adapter.notifyDataSetChanged()
            recorder.takeValue() shouldEqual adapter

            recorder.assertNoMoreValues()

            cancelTestScope()

            adapter.notifyDataSetChanged()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun adapterDataChanges_emitImmediately() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Adapter>(testScope)
            val adapter = TestAdapter()

            adapter.dataChanges(emitImmediately = true).recordWith(recorder)

            recorder.takeValue() shouldEqual adapter
            recorder.assertNoMoreValues()

            adapter.notifyDataSetChanged()
            recorder.takeValue() shouldEqual adapter

            recorder.assertNoMoreValues()

            cancelTestScope()

            adapter.notifyDataSetChanged()
            recorder.assertNoMoreValues()
        }
    }
}
