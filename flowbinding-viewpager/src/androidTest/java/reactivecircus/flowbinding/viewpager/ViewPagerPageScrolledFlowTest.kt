package reactivecircus.flowbinding.viewpager

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.viewpager.widget.ViewPager
import com.google.common.truth.Truth.assertThat
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
            assertThat(event.view)
                .isEqualTo(viewPager)
            assertThat(event.position)
                .isEqualTo(0)
            assertThat(event.positionOffset)
                .isGreaterThan(0f)
            assertThat(event.positionOffsetPixel)
                .isGreaterThan(0)

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
            assertThat(event.view)
                .isEqualTo(viewPager)
            assertThat(event.position)
                .isEqualTo(0)
            assertThat(event.positionOffset)
                .isGreaterThan(0f)
            assertThat(event.positionOffsetPixel)
                .isGreaterThan(0)

            cancelTestScope()
            recorder.clearValues()

            runOnUiThread { viewPager.currentItem = 0 }
            getInstrumentation().waitForIdleSync()
            recorder.assertNoMoreValues()
        }
    }
}
