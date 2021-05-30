package reactivecircus.flowbinding.android.view

import android.view.MotionEvent
import android.view.View
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.hoverMotionEventAtPosition
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewHoverFlowTest {

    @Test
    fun viewHovers() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<MotionEvent>(testScope)
            val view = View(rootView.context)
            view.hovers().recordWith(recorder)

            recorder.assertNoMoreValues()

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_HOVER_ENTER, 0, 50))
            assertThat(recorder.takeValue().action)
                .isEqualTo(MotionEvent.ACTION_HOVER_ENTER)

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_HOVER_MOVE, 1, 50))
            assertThat(recorder.takeValue().action)
                .isEqualTo(MotionEvent.ACTION_HOVER_MOVE)

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_HOVER_EXIT, 1, 50))
            assertThat(recorder.takeValue().action)
                .isEqualTo(MotionEvent.ACTION_HOVER_EXIT)

            cancelTestScope()

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_HOVER_ENTER, 0, 50))
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun viewHovers_notHandled() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<MotionEvent>(testScope)
            val view = View(rootView.context)
            view.hovers { false }
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_HOVER_ENTER, 0, 50))
            recorder.assertNoMoreValues()

            cancelTestScope()

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_HOVER_ENTER, 0, 50))
            recorder.assertNoMoreValues()
        }
    }
}
