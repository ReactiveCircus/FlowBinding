package reactivecircus.flowbinding.material

import android.view.MotionEvent
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
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
class RangeSliderChangeEventFlowTest {

    @Test
    fun rangeSliderChangeEvents() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<RangeSliderChangeEvent>(testScope)
            val rangeSlider = getViewById<RangeSlider>(R.id.rangeSlider)
            rangeSlider.changeEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_DOWN, 20, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 100, 50))
            val rangeSliderChangeEvent1 = recorder.takeValue()
            rangeSliderChangeEvent1.rangeSlider shouldEqual rangeSlider
            rangeSliderChangeEvent1.values shouldEqual listOf(100.0f)
            rangeSliderChangeEvent1.fromUser shouldEqual true
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 0, 50))
            val rangeSliderChangeEvent2 = recorder.takeValue()
            rangeSliderChangeEvent2.rangeSlider shouldEqual rangeSlider
            rangeSliderChangeEvent2.values shouldEqual listOf(0.0f)
            rangeSliderChangeEvent2.fromUser shouldEqual true
            recorder.assertNoMoreValues()

            cancelTestScope()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 50, 50))
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun rangeSliderChangeEvents_programmatic() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<RangeSliderChangeEvent>(testScope)
            val rangeSlider = getViewById<RangeSlider>(R.id.rangeSlider)
            rangeSlider.changeEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                rangeSlider.values = listOf(50.0f)
            }
            val rangeSliderChangeEvent1 = recorder.takeValue()
            rangeSliderChangeEvent1.rangeSlider shouldEqual rangeSlider
            rangeSliderChangeEvent1.values shouldEqual listOf(50.0f)
            rangeSliderChangeEvent1.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            runOnUiThread {
                rangeSlider.values = listOf(75.0f)
            }
            val rangeSliderChangeEvent2 = recorder.takeValue()
            rangeSliderChangeEvent2.rangeSlider shouldEqual rangeSlider
            rangeSliderChangeEvent2.values shouldEqual listOf(75.0f)
            rangeSliderChangeEvent2.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                rangeSlider.values = listOf(100.0f)
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun rangeSliderChangeEvents_emitImmediately() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<RangeSliderChangeEvent>(testScope)
            val rangeSlider = getViewById<RangeSlider>(R.id.rangeSlider)
            rangeSlider.changeEvents(emitImmediately = true).recordWith(recorder)

            val rangeSliderChangeEvent1 = recorder.takeValue()
            rangeSliderChangeEvent1.rangeSlider shouldEqual rangeSlider
            rangeSliderChangeEvent1.values shouldEqual listOf(0.0f)
            rangeSliderChangeEvent1.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            runOnUiThread {
                rangeSlider.values = listOf(50.0f)
            }
            val rangeSliderChangeEvent2 = recorder.takeValue()
            rangeSliderChangeEvent2.rangeSlider shouldEqual rangeSlider
            rangeSliderChangeEvent2.values shouldEqual listOf(50.0f)
            rangeSliderChangeEvent2.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                rangeSlider.values = listOf(100.0f)
            }
            recorder.assertNoMoreValues()
        }
    }
}
