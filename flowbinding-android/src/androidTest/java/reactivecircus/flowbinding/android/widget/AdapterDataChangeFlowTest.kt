package reactivecircus.flowbinding.android.widget

import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.BaseAdapter
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.ListFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AdapterDataChangeFlowTest {

    @Test
    fun adapterDataChanges() {
        launchTest<ListFragment> {
            val recorder = FlowRecorder<Adapter>(testScope)
            val adapter = TestAdapter()

            adapter.dataChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(adapter)
            recorder.assertNoMoreValues()

            adapter.notifyDataSetChanged()
            assertThat(recorder.takeValue())
                .isEqualTo(adapter)

            adapter.notifyDataSetChanged()
            assertThat(recorder.takeValue())
                .isEqualTo(adapter)

            recorder.assertNoMoreValues()

            cancelTestScope()

            adapter.notifyDataSetChanged()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun adapterDataChanges_skipInitialValue() {
        launchTest<ListFragment> {
            val recorder = FlowRecorder<Adapter>(testScope)
            val adapter = TestAdapter()

            adapter.dataChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            adapter.notifyDataSetChanged()
            assertThat(recorder.takeValue())
                .isEqualTo(adapter)

            recorder.assertNoMoreValues()

            cancelTestScope()

            adapter.notifyDataSetChanged()
            recorder.assertNoMoreValues()
        }
    }
}

private class TestAdapter : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? = null

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = 0
}
