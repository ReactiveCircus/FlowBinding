package reactivecircus.flowbinding.android.widget

import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.BaseAdapter
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
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
        launchTest<ListFragment> {
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

private class TestAdapter : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? = null

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = 0
}
