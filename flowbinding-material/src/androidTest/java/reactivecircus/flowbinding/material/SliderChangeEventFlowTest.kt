package reactivecircus.flowbinding.material

import android.view.MotionEvent
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
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
class SliderChangeEventFlowTest {

    @Test
    fun sliderChangeEvents() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<SliderChangeEvent>(testScope)
            val slider = getViewById<Slider>(R.id.slider)
            slider.changeEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_DOWN, 20, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
            val sliderChangeEvent1 = recorder.takeValue()
            sliderChangeEvent1.slider shouldEqual slider
            sliderChangeEvent1.value shouldEqual 1f
            sliderChangeEvent1.fromUser shouldEqual true
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 0, 50))
            val sliderChangeEvent2 = recorder.takeValue()
            sliderChangeEvent2.slider shouldEqual slider
            sliderChangeEvent2.value shouldEqual 0f
            sliderChangeEvent2.fromUser shouldEqual true
            recorder.assertNoMoreValues()

            cancelTestScope()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 50, 50))
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun sliderChangeEvents_programmatic() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<SliderChangeEvent>(testScope)
            val slider = getViewById<Slider>(R.id.slider)
            slider.changeEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                slider.value = 0.5f
            }
            val sliderChangeEvent1 = recorder.takeValue()
            sliderChangeEvent1.slider shouldEqual slider
            sliderChangeEvent1.value shouldEqual 0.5f
            sliderChangeEvent1.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            runOnUiThread {
                slider.value = 0.75f
            }
            val sliderChangeEvent2 = recorder.takeValue()
            sliderChangeEvent2.slider shouldEqual slider
            sliderChangeEvent2.value shouldEqual 0.75f
            sliderChangeEvent2.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                slider.value = 1f
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun sliderChangeEvents_emitImmediately() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<SliderChangeEvent>(testScope)
            val slider = getViewById<Slider>(R.id.slider)
            slider.changeEvents(emitImmediately = true).recordWith(recorder)

            val sliderChangeEvent1 = recorder.takeValue()
            sliderChangeEvent1.slider shouldEqual slider
            sliderChangeEvent1.value shouldEqual 0f
            sliderChangeEvent1.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            runOnUiThread {
                slider.value = 0.5f
            }
            val sliderChangeEvent2 = recorder.takeValue()
            sliderChangeEvent2.slider shouldEqual slider
            sliderChangeEvent2.value shouldEqual 0.5f
            sliderChangeEvent2.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                slider.value = 1f
            }
            recorder.assertNoMoreValues()
        }
    }
}
