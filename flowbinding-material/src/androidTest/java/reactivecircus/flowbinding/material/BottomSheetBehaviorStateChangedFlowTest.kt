package reactivecircus.flowbinding.material

import android.view.View
import androidx.test.filters.LargeTest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.swipeDownOnView
import reactivecircus.blueprint.testing.action.swipeUpOnView
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class BottomSheetBehaviorStateChangedFlowTest {

    @Test
    fun bottomSheetStateChanges_drag() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val bottomSheet = getViewById<View>(R.id.bottomSheetLayout)
            bottomSheet.bottomSheetStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeDownOnView(R.id.textViewInBottomSheet)
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_DRAGGING)
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_SETTLING)
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_COLLAPSED)

            swipeUpOnView(R.id.textViewInBottomSheet)
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_DRAGGING)
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_SETTLING)
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_EXPANDED)

            cancelTestScope()
            recorder.clearValues()

            swipeUpOnView(R.id.textViewInBottomSheet)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun bottomSheetStateChanges_programmatic() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val bottomSheet = getViewById<View>(R.id.bottomSheetLayout)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.bottomSheetStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            // STATE_DRAGGING state is not emitted for programmatic state change
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_SETTLING)
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_EXPANDED)

            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            // STATE_DRAGGING state is not emitted for programmatic state change
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_SETTLING)
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_COLLAPSED)

            behavior.state = BottomSheetBehavior.STATE_HIDDEN
            // STATE_DRAGGING state is not emitted for programmatic state change
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_SETTLING)
            assertThat(recorder.takeValue()).isEqualTo(BottomSheetBehavior.STATE_HIDDEN)

            cancelTestScope()
            recorder.clearValues()

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            recorder.assertNoMoreValues()
        }
    }
}
