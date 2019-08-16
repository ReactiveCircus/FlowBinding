package reactivecircus.flowbinding.viewpager2

import androidx.test.filters.LargeTest
import androidx.viewpager2.widget.ViewPager2
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.cancel
import org.junit.Test
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith
import reactivecircus.flowbinding.viewpager2.test.R

@LargeTest
class ViewPager2PageSelectedFlowTest {

    @Test
    fun pageSelections_swipe() {
        launchTest<ViewPager2Fragment> {
            val recorder = FlowRecorder<Int>(testScope)
            getViewById<ViewPager2>(R.id.viewPager)
                .pageSelections()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeLeftOnView(R.id.viewPager)
            assertThat(recorder.takeValue()).isEqualTo(1)
            recorder.assertNoMoreValues()

            testScope.cancel()

            swipeRightOnView(R.id.viewPager)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun pageSelections_programmatic() {
        launchTest<ViewPager2Fragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val viewPager = getViewById<ViewPager2>(R.id.viewPager)
            viewPager.pageSelections().recordWith(recorder)

            recorder.assertNoMoreValues()

            viewPager.currentItem = 1
            assertThat(recorder.takeValue()).isEqualTo(1)
            recorder.assertNoMoreValues()

            testScope.cancel()

            viewPager.currentItem = 0
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun pageSelections_emitImmediately() {
        launchTest<ViewPager2Fragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val viewPager = getViewById<ViewPager2>(R.id.viewPager)
            viewPager.pageSelections(emitImmediately = true).recordWith(recorder)

            assertThat(recorder.takeValue()).isEqualTo(0)
            recorder.assertNoMoreValues()

            viewPager.currentItem = 1
            assertThat(recorder.takeValue()).isEqualTo(1)
            recorder.assertNoMoreValues()

            testScope.cancel()

            viewPager.currentItem = 0
            recorder.assertNoMoreValues()
        }
    }
}
