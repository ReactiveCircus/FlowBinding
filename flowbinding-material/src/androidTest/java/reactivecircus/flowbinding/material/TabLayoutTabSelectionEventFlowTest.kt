package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.filterIsInstance
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class TabLayoutTabSelectionEventFlowTest {

    @Test
    fun tabSelectionEvents_tabSelected() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<TabLayoutSelectionEvent.TabSelected>(testScope)
            val tabLayout = getViewById<TabLayout>(R.id.tabLayout)
            tabLayout.tabSelectionEvents()
                .filterIsInstance<TabLayoutSelectionEvent.TabSelected>()
                .recordWith(recorder)

            recorder.takeValue().tab.text shouldEqual tabLayout.getTabAt(0)?.text
            recorder.assertNoMoreValues()

            runOnUiThread { tabLayout.getTabAt(1)?.select() }
            recorder.takeValue().tab.text shouldEqual tabLayout.getTabAt(1)?.text
            recorder.assertNoMoreValues()

            runOnUiThread { tabLayout.getTabAt(2)?.select() }
            recorder.takeValue().tab.text shouldEqual tabLayout.getTabAt(2)?.text
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { tabLayout.getTabAt(0)?.select() }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun tabSelectionEvents_tabReselected() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<TabLayoutSelectionEvent.TabReselected>(testScope)
            val tabLayout = getViewById<TabLayout>(R.id.tabLayout)
            tabLayout.tabSelectionEvents()
                .filterIsInstance<TabLayoutSelectionEvent.TabReselected>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { tabLayout.getTabAt(1)?.select() }
            recorder.assertNoMoreValues()

            runOnUiThread { tabLayout.getTabAt(1)?.select() }
            recorder.takeValue().tab.text shouldEqual tabLayout.getTabAt(1)?.text
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { tabLayout.getTabAt(2)?.select() }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun tabSelectionEvents_tabUnselected() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<TabLayoutSelectionEvent.TabUnselected>(testScope)
            val tabLayout = getViewById<TabLayout>(R.id.tabLayout)
            tabLayout.tabSelectionEvents()
                .filterIsInstance<TabLayoutSelectionEvent.TabUnselected>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { tabLayout.getTabAt(1)?.select() }
            recorder.takeValue().tab.text shouldEqual tabLayout.getTabAt(0)?.text
            recorder.assertNoMoreValues()

            runOnUiThread { tabLayout.getTabAt(2)?.select() }
            recorder.takeValue().tab.text shouldEqual tabLayout.getTabAt(1)?.text
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { tabLayout.getTabAt(0)?.select() }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun tabSelectionEvents_tabSelected_skipInitialValue() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<TabLayoutSelectionEvent.TabSelected>(testScope)
            val tabLayout = getViewById<TabLayout>(R.id.tabLayout)
            tabLayout.tabSelectionEvents()
                .skipInitialValue()
                .filterIsInstance<TabLayoutSelectionEvent.TabSelected>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { tabLayout.getTabAt(1)?.select() }
            recorder.takeValue().tab.text shouldEqual tabLayout.getTabAt(1)?.text
            recorder.assertNoMoreValues()

            runOnUiThread { tabLayout.getTabAt(2)?.select() }
            recorder.takeValue().tab.text shouldEqual tabLayout.getTabAt(2)?.text
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { tabLayout.getTabAt(0)?.select() }
            recorder.assertNoMoreValues()
        }
    }
}
