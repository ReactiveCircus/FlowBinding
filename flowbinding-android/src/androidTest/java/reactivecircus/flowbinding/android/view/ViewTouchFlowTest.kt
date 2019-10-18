package reactivecircus.flowbinding.android.view

import android.view.MotionEvent
import android.view.View
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.hoverMotionEventAtPosition
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewTouchFlowTest {

    @Test
    fun viewTouches() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<MotionEvent>(testScope)
            val view = getViewById<View>(R.id.draggableView)
            view.touches().recordWith(recorder)

            recorder.assertNoMoreValues()

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_DOWN, 0, 50))
            recorder.takeValue().action shouldEqual MotionEvent.ACTION_DOWN

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_MOVE, 1, 50))
            recorder.takeValue().action shouldEqual MotionEvent.ACTION_MOVE

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_HOVER_EXIT, 1, 50))
            recorder.takeValue().action shouldEqual MotionEvent.ACTION_HOVER_EXIT

            cancelTestScope()

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_UP, 1, 50))
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun viewTouches_notHandled() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<MotionEvent>(testScope)
            val view = getViewById<View>(R.id.draggableView)
            view.touches() { false }
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_DOWN, 0, 50))
            recorder.assertNoMoreValues()

            cancelTestScope()

            view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, MotionEvent.ACTION_DOWN, 0, 50))
            recorder.assertNoMoreValues()
        }
    }
}
