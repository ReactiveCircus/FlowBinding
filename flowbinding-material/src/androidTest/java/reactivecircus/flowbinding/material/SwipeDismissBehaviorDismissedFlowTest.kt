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
class SwipeDismissBehaviorDismissedFlowTest {

    @Test
    fun swipeDismissBehaviorDismisses() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<View>(testScope)
            val viewToDismiss1 = getViewById<MaterialCardView>(R.id.materialCardViewTop)
            (viewToDismiss1.layoutParams as CoordinatorLayout.LayoutParams).behavior =
                SwipeDismissBehavior<View>()
            viewToDismiss1.dismisses().recordWith(recorder)
            recorder.assertNoMoreValues()

            hardSwipeRight(R.id.materialCardViewTop)
            val dismissedView = recorder.takeValue()
            assertThat(dismissedView).isEqualTo(viewToDismiss1)

            cancelTestScope()
            recorder.clearValues()

            val viewToDismiss2 = getViewById<MaterialCardView>(R.id.materialCardViewBottom)
            (viewToDismiss2.layoutParams as CoordinatorLayout.LayoutParams).behavior =
                SwipeDismissBehavior<View>()
            viewToDismiss2.dismisses().recordWith(recorder)
            hardSwipeLeft(R.id.materialCardViewBottom)
            recorder.assertNoMoreValues()
        }
    }
}
