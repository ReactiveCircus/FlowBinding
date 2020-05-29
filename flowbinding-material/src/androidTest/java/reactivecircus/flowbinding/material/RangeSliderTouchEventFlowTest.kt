package reactivecircus.flowbinding.material

import android.view.MotionEvent
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.android.material.slider.RangeSlider
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.motionEventAtPosition
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class RangeSliderTouchEventFlowTest {

    @Test
    fun rangeSliderTouchEvents() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<RangeSliderTouchEvent>(testScope)
            val rangeSlider = getViewById<RangeSlider>(R.id.rangeSlider)
            rangeSlider.touchEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_DOWN, 20, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 50, 50))
            val touchEvent1 = recorder.takeValue() as RangeSliderTouchEvent.StartTracking
            touchEvent1.rangeSlider shouldEqual rangeSlider
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 100, 50))
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_UP, 100, 50))
            val touchEvent2 = recorder.takeValue() as RangeSliderTouchEvent.StopTracking
            touchEvent2.rangeSlider shouldEqual rangeSlider
            recorder.assertNoMoreValues()

            cancelTestScope()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_DOWN, 80, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 50, 50))
            recorder.assertNoMoreValues()
        }
    }
}
