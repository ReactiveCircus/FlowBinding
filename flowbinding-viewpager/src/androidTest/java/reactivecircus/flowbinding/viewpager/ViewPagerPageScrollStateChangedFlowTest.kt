package reactivecircus.flowbinding.viewpager

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
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
class ViewPagerPageScrollStateChangedFlowTest {

    @Test
    fun pageScrollStateChanges_swipe() {
        launchTest<ViewPagerFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            getViewById<ViewPager>(R.id.viewPager).pageScrollStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeLeftOnView(R.id.viewPager)
            assertThat(recorder.takeValue())
                .isEqualTo(ViewPager.SCROLL_STATE_DRAGGING)
            assertThat(recorder.takeValue())
                .isEqualTo(ViewPager.SCROLL_STATE_SETTLING)
            assertThat(recorder.takeValue())
                .isEqualTo(ViewPager.SCROLL_STATE_IDLE)
            recorder.assertNoMoreValues()

            cancelTestScope()

            swipeRightOnView(R.id.viewPager)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun pageScrollStateChanges_programmatic() {
        launchTest<ViewPagerFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val viewPager = getViewById<ViewPager>(R.id.viewPager)
            viewPager.pageScrollStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { viewPager.currentItem = 1 }
            // SCROLL_STATE_DRAGGING state is not emitted for programmatic page change
            assertThat(recorder.takeValue())
                .isEqualTo(ViewPager.SCROLL_STATE_SETTLING)
            assertThat(recorder.takeValue())
                .isEqualTo(ViewPager.SCROLL_STATE_IDLE)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { viewPager.currentItem = 0 }
            recorder.assertNoMoreValues()
        }
    }
}
