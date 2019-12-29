package reactivecircus.flowbinding.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import kotlinx.coroutines.flow.filterIsInstance
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.recyclerview.fixtures.RecyclerViewFragment
import reactivecircus.flowbinding.recyclerview.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class RecyclerViewChildAttachStateChangeEventFlowTest {

    @Test
    fun recyclerViewChildAttachStateChangeEvents_attached() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<RecyclerViewChildAttachStateChangeEvent>(testScope)
            val recyclerView = getViewById<RecyclerView>(R.id.recyclerView)
            val childView = View(rootView.context)
            recyclerView.childAttachStateChangeEvents()
                .filterIsInstance<RecyclerViewChildAttachStateChangeEvent.Attached>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            val simpleAdapter = SimpleAdapter(childView)
            runOnUiThread {
                recyclerView.adapter = simpleAdapter
            }
            val event = recorder.takeValue()
            event.view shouldEqual recyclerView
            event.child shouldEqual childView

            cancelTestScope()
            recorder.clearValues()

            runOnUiThread {
                recyclerView.adapter = simpleAdapter
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun recyclerViewChildAttachStateChangeEvents_detached() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<RecyclerViewChildAttachStateChangeEvent>(testScope)
            val childView = View(rootView.context)
            val simpleAdapter = SimpleAdapter(childView)
            val recyclerView = getViewById<RecyclerView>(R.id.recyclerView).apply {
                runOnUiThread {
                    adapter = simpleAdapter
                }
            }
            recyclerView.childAttachStateChangeEvents()
                .filterIsInstance<RecyclerViewChildAttachStateChangeEvent.Detached>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                recyclerView.adapter = null
            }
            val event = recorder.takeValue()
            event.view shouldEqual recyclerView
            event.child shouldEqual childView

            cancelTestScope()
            recorder.clearValues()

            runOnUiThread {
                recyclerView.adapter = simpleAdapter
                recyclerView.adapter = null
            }
            recorder.assertNoMoreValues()
        }
    }
}

private class SimpleAdapter(private val child: View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(child) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    override fun getItemCount(): Int = 1
}
