package reactivecircus.flowbinding.material

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.test.filters.LargeTest
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.card.MaterialCardView
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.hardSwipeLeft
import reactivecircus.flowbinding.testing.hardSwipeRight
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SwipeDismissBehaviorDragStateChangedFlowTest {

    @Test
    fun swipeDismissBehaviorDragStateChanges() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val viewToDismiss1 = getViewById<MaterialCardView>(R.id.materialCardViewTop)
            (viewToDismiss1.layoutParams as CoordinatorLayout.LayoutParams).behavior =
                SwipeDismissBehavior<View>()
            viewToDismiss1.dragStateChanges().recordWith(recorder)
            recorder.assertNoMoreValues()

            hardSwipeRight(R.id.materialCardViewTop)
            assertThat(recorder.takeValue()).isEqualTo(SwipeDismissBehavior.STATE_DRAGGING)
            assertThat(recorder.takeValue()).isEqualTo(SwipeDismissBehavior.STATE_SETTLING)
            assertThat(recorder.takeValue()).isEqualTo(SwipeDismissBehavior.STATE_IDLE)

            cancelTestScope()
            recorder.clearValues()

            val viewToDismiss2 = getViewById<MaterialCardView>(R.id.materialCardViewBottom)
            (viewToDismiss2.layoutParams as CoordinatorLayout.LayoutParams).behavior =
                SwipeDismissBehavior<View>()
            viewToDismiss2.dragStateChanges().recordWith(recorder)
            hardSwipeLeft(R.id.materialCardViewBottom)
            recorder.assertNoMoreValues()
        }
    }
}
