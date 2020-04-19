package reactivecircus.flowbinding.viewpager

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.viewpager.widget.ViewPager
import org.amshove.kluent.shouldBeGreaterThan
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
class ViewPagerPageScrolledFlowTest {

    @Test
    fun pageScrollEvents_swipe() {
        launchTest<ViewPagerFragment> {
            val recorder = FlowRecorder<ViewPagerPageScrollEvent>(testScope)
            val viewPager = getViewById<ViewPager>(R.id.viewPager)
            viewPager.pageScrollEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeLeftOnView(R.id.viewPager)
            val event = recorder.takeValue()
            event.view shouldEqual viewPager
            event.position shouldEqual 0
            event.positionOffset shouldBeGreaterThan 0f
            event.positionOffsetPixel shouldBeGreaterThan 0

            cancelTestScope()
            recorder.clearValues()

            swipeRightOnView(R.id.viewPager)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun pageScrollEvents_programmatic() {
        launchTest<ViewPagerFragment> {
            val recorder = FlowRecorder<ViewPagerPageScrollEvent>(testScope)
            val viewPager = getViewById<ViewPager>(R.id.viewPager)
            viewPager.pageScrollEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { viewPager.currentItem = 1 }
            getInstrumentation().waitForIdleSync()
            val event = recorder.takeValue()
            event.view shouldEqual viewPager
            event.position shouldEqual 0
            event.positionOffset shouldBeGreaterThan 0f
            event.positionOffsetPixel shouldBeGreaterThan 0

            cancelTestScope()
            recorder.clearValues()

            runOnUiThread { viewPager.currentItem = 0 }
            getInstrumentation().waitForIdleSync()
            recorder.assertNoMoreValues()
        }
    }
}
