package reactivecircus.flowbinding.material

import android.view.MotionEvent
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
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
            recorder.takeValue().also { sliderChangeEvent ->
                sliderChangeEvent.slider shouldEqual slider
                sliderChangeEvent.value shouldEqual 1f
                sliderChangeEvent.fromUser shouldEqual true
            }
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 0, 50))
            recorder.takeValue().also { sliderChangeEvent ->
                sliderChangeEvent.slider shouldEqual slider
                sliderChangeEvent.value shouldEqual 0f
                sliderChangeEvent.fromUser shouldEqual true
            }
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

            UiThreadStatement.runOnUiThread {
                slider.value = 0.5f
            }
            recorder.takeValue().also { sliderChangeEvent ->
                sliderChangeEvent.slider shouldEqual slider
                sliderChangeEvent.value shouldEqual 0.5f
                sliderChangeEvent.fromUser shouldEqual false
            }
            recorder.assertNoMoreValues()

            UiThreadStatement.runOnUiThread {
                slider.value = 0.75f
            }
            recorder.takeValue().also { sliderChangeEvent ->
                sliderChangeEvent.slider shouldEqual slider
                sliderChangeEvent.value shouldEqual 0.75f
                sliderChangeEvent.fromUser shouldEqual false
            }
            recorder.assertNoMoreValues()

            cancelTestScope()

            UiThreadStatement.runOnUiThread {
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

            recorder.takeValue().also { sliderChangeEvent ->
                sliderChangeEvent.slider shouldEqual slider
                sliderChangeEvent.value shouldEqual 0f
                sliderChangeEvent.fromUser shouldEqual false
            }
            recorder.assertNoMoreValues()

            UiThreadStatement.runOnUiThread {
                slider.value = 0.5f
            }
            recorder.takeValue().also { sliderChangeEvent ->
                sliderChangeEvent.slider shouldEqual slider
                sliderChangeEvent.value shouldEqual 0.5f
                sliderChangeEvent.fromUser shouldEqual false
            }
            recorder.assertNoMoreValues()

            cancelTestScope()

            UiThreadStatement.runOnUiThread {
                slider.value = 1f
            }
            recorder.assertNoMoreValues()
        }
    }
}
