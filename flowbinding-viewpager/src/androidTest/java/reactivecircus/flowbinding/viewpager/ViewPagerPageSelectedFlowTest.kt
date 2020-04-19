package reactivecircus.flowbinding.viewpager

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.viewpager.widget.ViewPager
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.swipeLeftOnView
import reactivecircus.blueprint.testing.action.swipeRightOnView
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith
import reactivecircus.flowbinding.viewpager.fixtures.ViewPagerFragment
import reactivecircus.flowbinding.viewpager.test.R

@LargeTest
class ViewPagerPageSelectedFlowTest {

    @Test
    fun pageSelections_swipe() {
        launchTest<ViewPagerFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            getViewById<ViewPager>(R.id.viewPager).pageSelections().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeLeftOnView(R.id.viewPager)
            recorder.takeValue() shouldEqual 1
            recorder.assertNoMoreValues()

            cancelTestScope()

            swipeRightOnView(R.id.viewPager)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun pageSelections_programmatic() {
        launchTest<ViewPagerFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val viewPager = getViewById<ViewPager>(R.id.viewPager)
            viewPager.pageSelections().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { viewPager.currentItem = 1 }
            recorder.takeValue() shouldEqual 1
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { viewPager.currentItem = 0 }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun pageSelections_emitImmediately() {
        launchTest<ViewPagerFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val viewPager = getViewById<ViewPager>(R.id.viewPager)
            viewPager.pageSelections(emitImmediately = true).recordWith(recorder)

            recorder.takeValue() shouldEqual 0
            recorder.assertNoMoreValues()

            runOnUiThread { viewPager.currentItem = 1 }
            recorder.takeValue() shouldEqual 1
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { viewPager.currentItem = 0 }
            recorder.assertNoMoreValues()
        }
    }
}
