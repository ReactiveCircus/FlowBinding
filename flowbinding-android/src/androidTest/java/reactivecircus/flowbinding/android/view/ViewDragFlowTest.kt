package reactivecircus.flowbinding.android.view

import android.view.DragEvent
import android.view.View
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Ignore
import org.junit.Test
import reactivecircus.blueprint.testing.action.longClickView
import reactivecircus.blueprint.testing.action.swipeUpOnView
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@Ignore("Testing drag-n-drop with Espresso is unsupported: https://github.com/android/android-test/issues/228")
@LargeTest
class ViewDragFlowTest {

    @Test
    fun viewDrags() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<DragEvent>(testScope)
            val draggableView = getViewById<View>(R.id.draggableView)
            draggableView.drags().recordWith(recorder)

            recorder.assertNoMoreValues()

            longClickView(R.id.draggableView)
            assertThat(recorder.takeValue().action)
                .isEqualTo(DragEvent.ACTION_DRAG_STARTED)
            recorder.assertNoMoreValues()

            cancelTestScope()

            longClickView(R.id.draggableView)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun viewDrags_notHandled() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<DragEvent>(testScope)
            val view = getViewById<View>(R.id.parentView)
            view.drags { false }.recordWith(recorder)

            recorder.assertNoMoreValues()

            longClickView(R.id.draggableView)
            recorder.assertNoMoreValues()

            cancelTestScope()

            swipeUpOnView(R.id.parentView)
            recorder.assertNoMoreValues()
        }
    }
}
