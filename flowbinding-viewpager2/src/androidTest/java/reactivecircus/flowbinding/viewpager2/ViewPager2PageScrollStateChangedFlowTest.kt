package reactivecircus.flowbinding.viewpager2

import androidx.test.filters.LargeTest
import androidx.viewpager2.widget.ViewPager2
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.cancel
import org.junit.Test
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith
import reactivecircus.flowbinding.testing.ui.ViewPager2Fragment
import reactivecircus.flowbinding.viewpager2.test.R

@LargeTest
class ViewPager2PageScrollStateChangedFlowTest {

    @Test
    fun pageScrollStateChanges_swipe() {
        launchTest<ViewPager2Fragment> {
            val recorder = FlowRecorder<Int>(testScope)
            getViewById<ViewPager2>(R.id.viewPager)
                .pageScrollStateChanges()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeLeftOnView(R.id.viewPager)
            assertThat(recorder.takeValue()).isEqualTo(ViewPager2.SCROLL_STATE_DRAGGING)
            assertThat(recorder.takeValue()).isEqualTo(ViewPager2.SCROLL_STATE_SETTLING)
            assertThat(recorder.takeValue()).isEqualTo(ViewPager2.SCROLL_STATE_IDLE)
            recorder.assertNoMoreValues()

            testScope.cancel()

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

            testScope.cancel()

            viewPager.currentItem = 0
            recorder.assertNoMoreValues()
        }
    }
}
