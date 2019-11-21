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
class SliderValueChangeFlowTest {

    @Test
    fun sliderValueChanges() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Float>(testScope)
            val slider = getViewById<Slider>(R.id.slider)
            slider.valueChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_DOWN, 0, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
            recorder.takeValue() shouldEqual 0f
            recorder.takeValue() shouldEqual 1f
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 0, 50))
            recorder.takeValue() shouldEqual 0f
            recorder.assertNoMoreValues()

            cancelTestScope()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 50, 50))
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun sliderValueChanges_programmatic() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Float>(testScope)
            val slider = getViewById<Slider>(R.id.slider)
            slider.valueChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                slider.value = 0.5f
            }
            recorder.takeValue() shouldEqual 0.5f
            recorder.assertNoMoreValues()

            runOnUiThread {
                slider.value = 0.75f
            }
            recorder.takeValue() shouldEqual 0.75f
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                slider.value = 1f
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun sliderValueChanges_emitImmediately() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Float>(testScope)
            val slider = getViewById<Slider>(R.id.slider)
            slider.valueChanges(emitImmediately = true).recordWith(recorder)

            recorder.takeValue() shouldEqual 0f
            recorder.assertNoMoreValues()

            runOnUiThread {
                slider.value = 0.5f
            }
            recorder.takeValue() shouldEqual 0.5f
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                slider.value = 1f
            }
            recorder.assertNoMoreValues()
        }
    }
}
