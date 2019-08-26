package reactivecircus.flowbinding.viewpager2

import androidx.test.filters.LargeTest
import androidx.viewpager2.widget.ViewPager2
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.swipeLeftOnView
import reactivecircus.blueprint.testing.action.swipeRightOnView
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith
import reactivecircus.flowbinding.viewpager2.fixtures.ViewPager2Fragment
import reactivecircus.flowbinding.viewpager2.test.R

@LargeTest
class ViewPager2PageScrolledFlowTest {

    @Test
    fun pageScrollEvents_swipe() {
        launchTest<ViewPager2Fragment> {
            val recorder = FlowRecorder<ViewPager2PageScrollEvent>(testScope)
            getViewById<ViewPager2>(R.id.viewPager).pageScrollEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeLeftOnView(R.id.viewPager)
            val event = recorder.takeValue()
            assertThat(event.position).isEqualTo(0)
            assertThat(event.positionOffset).isGreaterThan(0f)
            assertThat(event.positionOffsetPixel).isGreaterThan(0)

            cancelTestScope()
            recorder.clearValues()

            swipeRightOnView(R.id.viewPager)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun pageScrollEvents_programmatic() {
        launchTest<ViewPager2Fragment> {
            val recorder = FlowRecorder<ViewPager2PageScrollEvent>(testScope)
            val viewPager = getViewById<ViewPager2>(R.id.viewPager)
            viewPager.pageScrollEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            viewPager.currentItem = 1
            val event = recorder.takeValue()
            assertThat(event.position).isEqualTo(0)
            assertThat(event.positionOffset).isGreaterThan(0f)
            assertThat(event.positionOffsetPixel).isGreaterThan(0)

            cancelTestScope()
            recorder.clearValues()

            viewPager.currentItem = 0
            recorder.assertNoMoreValues()
        }
    }
}
