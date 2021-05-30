package reactivecircus.flowbinding.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.recyclerview.fixtures.RecyclerViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class RecyclerViewAdapterDataChangeFlowTest {

    @Test
    fun recyclerViewAdapterDataChanges() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<RecyclerView.Adapter<RecyclerView.ViewHolder>>(testScope)
            val adapter = TestRecyclerViewAdapter()

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
    fun recyclerViewAdapterDataChanges_skipInitialValue() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<RecyclerView.Adapter<RecyclerView.ViewHolder>>(testScope)
            val adapter = TestRecyclerViewAdapter()

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

private class TestRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            android.R.layout.simple_list_item_1,
            parent,
            false
        )
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    override fun getItemCount(): Int = 0
}
