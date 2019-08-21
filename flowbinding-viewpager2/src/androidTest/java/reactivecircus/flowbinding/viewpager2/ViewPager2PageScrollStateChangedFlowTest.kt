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
import reactivecircus.flowbinding.viewpager2.test.R

@LargeTest
class ViewPager2PageScrollStateChangedFlowTest {

    @Test
    fun pageScrollStateChanges_swipe() {
        launchTest<ViewPager2Fragment> {
            val recorder = FlowRecorder<Int>(testScope)
            getViewById<ViewPager2>(R.id.viewPager).pageScrollStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeLeftOnView(R.id.viewPager)
            assertThat(recorder.takeValue()).isEqualTo(ViewPager2.SCROLL_STATE_DRAGGING)
            assertThat(recorder.takeValue()).isEqualTo(ViewPager2.SCROLL_STATE_SETTLING)
            assertThat(recorder.takeValue()).isEqualTo(ViewPager2.SCROLL_STATE_IDLE)
            recorder.assertNoMoreValues()

            cancelTestScope()

            swipeRightOnView(R.id.viewPager)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun pageScrollStateChanges_programmatic() {
        launchTest<ViewPager2Fragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val viewPager = getViewById<ViewPager2>(R.id.viewPager)
            viewPager.pageScrollStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            viewPager.currentItem = 1
            // SCROLL_STATE_DRAGGING state is not emitted for programmatic page change
            assertThat(recorder.takeValue()).isEqualTo(ViewPager2.SCROLL_STATE_SETTLING)
            assertThat(recorder.takeValue()).isEqualTo(ViewPager2.SCROLL_STATE_IDLE)
            recorder.assertNoMoreValues()

            cancelTestScope()

            viewPager.currentItem = 0
            recorder.assertNoMoreValues()
        }
    }
}
