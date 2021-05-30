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
class ViewPager2PageSelectedFlowTest {

    @Test
    fun pageSelections_swipe() {
        launchTest<ViewPager2Fragment> {
            val recorder = FlowRecorder<Int>(testScope)
            getViewById<ViewPager2>(R.id.viewPager).pageSelections().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            swipeLeftOnView(R.id.viewPager)
            assertThat(recorder.takeValue())
                .isEqualTo(1)
            recorder.assertNoMoreValues()

            cancelTestScope()

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

            assertThat(recorder.takeValue())
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            viewPager.currentItem = 1
            assertThat(recorder.takeValue())
                .isEqualTo(1)
            recorder.assertNoMoreValues()

            cancelTestScope()

            viewPager.currentItem = 0
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun pageSelections_skipInitialValue() {
        launchTest<ViewPager2Fragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val viewPager = getViewById<ViewPager2>(R.id.viewPager)
            viewPager.pageSelections()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            viewPager.currentItem = 1
            assertThat(recorder.takeValue())
                .isEqualTo(1)
            recorder.assertNoMoreValues()

            cancelTestScope()

            viewPager.currentItem = 0
            recorder.assertNoMoreValues()
        }
    }
}
