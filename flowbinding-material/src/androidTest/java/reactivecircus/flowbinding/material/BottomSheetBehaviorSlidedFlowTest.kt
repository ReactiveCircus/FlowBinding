package reactivecircus.flowbinding.material

import android.view.View
import androidx.test.filters.LargeTest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.swipeDownOnView
import reactivecircus.blueprint.testing.action.swipeUpOnView
import reactivecircus.flowbinding.material.fixtures.MaterialFragment
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class BottomSheetBehaviorSlidedFlowTest {

    @Test
    fun bottomSheetSlides_drag() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<Float>(testScope)
            val bottomSheet = getViewById<View>(R.id.bottomSheetLayout)
            bottomSheet.bottomSheetSlides().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeUpOnView(R.id.textViewInBottomSheet)
            val event = recorder.takeValue()
            assertThat(event).isGreaterThan(0)

            cancelTestScope()
            recorder.clearValues()

            swipeDownOnView(R.id.textViewInBottomSheet)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun bottomSheetSlides_programmatic() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<Float>(testScope)
            val bottomSheet = getViewById<View>(R.id.bottomSheetLayout)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.bottomSheetSlides().recordWith(recorder)

            recorder.assertNoMoreValues()

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            val event = recorder.takeValue()
            assertThat(event).isGreaterThan(0)

            cancelTestScope()
            recorder.clearValues()

            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            recorder.assertNoMoreValues()
        }
    }
}
