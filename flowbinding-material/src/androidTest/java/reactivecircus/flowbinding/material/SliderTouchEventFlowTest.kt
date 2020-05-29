package reactivecircus.flowbinding.material

import android.view.MotionEvent
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.android.material.slider.Slider
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.motionEventAtPosition
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SliderTouchEventFlowTest {

    @Test
    fun sliderTouchEvents() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<SliderTouchEvent>(testScope)
            val slider = getViewById<Slider>(R.id.slider)
            slider.touchEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_DOWN, 20, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 50, 50))
            val touchEvent1 = recorder.takeValue() as SliderTouchEvent.StartTracking
            touchEvent1.slider shouldEqual slider
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_UP, 100, 50))
            val touchEvent2 = recorder.takeValue() as SliderTouchEvent.StopTracking
            touchEvent2.slider shouldEqual slider
            recorder.assertNoMoreValues()

            cancelTestScope()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_DOWN, 80, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 50, 50))
            recorder.assertNoMoreValues()
        }
    }
}
