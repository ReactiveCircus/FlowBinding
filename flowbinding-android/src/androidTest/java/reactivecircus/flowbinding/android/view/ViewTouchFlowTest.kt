package reactivecircus.flowbinding.android.view

import android.view.MotionEvent
import android.view.View
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.motionEventAtPosition
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewTouchFlowTest {

    @Test
    fun viewTouches() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<MotionEvent>(testScope)
            val view = View(rootView.context)
            view.touches().recordWith(recorder)

            recorder.assertNoMoreValues()

            view.dispatchTouchEvent(motionEventAtPosition(view, MotionEvent.ACTION_DOWN, 0, 50))
            assertThat(recorder.takeValue().action)
                .isEqualTo(MotionEvent.ACTION_DOWN)

            view.dispatchTouchEvent(motionEventAtPosition(view, MotionEvent.ACTION_MOVE, 1, 50))
            assertThat(recorder.takeValue().action)
                .isEqualTo(MotionEvent.ACTION_MOVE)

            cancelTestScope()

            view.dispatchTouchEvent(motionEventAtPosition(view, MotionEvent.ACTION_UP, 1, 50))
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun viewTouches_notHandled() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<MotionEvent>(testScope)
            val view = View(rootView.context)
            view.touches { false }
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            view.dispatchTouchEvent(motionEventAtPosition(view, MotionEvent.ACTION_DOWN, 0, 50))
            recorder.assertNoMoreValues()

            cancelTestScope()

            view.dispatchTouchEvent(motionEventAtPosition(view, MotionEvent.ACTION_DOWN, 0, 50))
            recorder.assertNoMoreValues()
        }
    }
}
