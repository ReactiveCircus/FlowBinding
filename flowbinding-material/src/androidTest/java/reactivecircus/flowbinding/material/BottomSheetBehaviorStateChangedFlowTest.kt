package reactivecircus.flowbinding.material

import android.view.View
import androidx.test.filters.LargeTest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class BottomSheetBehaviorStateChangedFlowTest {

    @Test
    fun bottomSheetStateChanges() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Int>(testScope)
            val bottomSheet = getViewById<View>(R.id.bottomSheetLayout)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.bottomSheetStateChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            // STATE_DRAGGING state is not emitted for programmatic state change
            recorder.takeValue() shouldEqual BottomSheetBehavior.STATE_SETTLING
            recorder.takeValue() shouldEqual BottomSheetBehavior.STATE_EXPANDED

            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            // STATE_DRAGGING state is not emitted for programmatic state change
            recorder.takeValue() shouldEqual BottomSheetBehavior.STATE_SETTLING
            recorder.takeValue() shouldEqual BottomSheetBehavior.STATE_COLLAPSED

            behavior.state = BottomSheetBehavior.STATE_HIDDEN
            // STATE_DRAGGING state is not emitted for programmatic state change
            recorder.takeValue() shouldEqual BottomSheetBehavior.STATE_SETTLING
            recorder.takeValue() shouldEqual BottomSheetBehavior.STATE_HIDDEN

            cancelTestScope()
            recorder.clearValues()

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            recorder.assertNoMoreValues()
        }
    }
}
