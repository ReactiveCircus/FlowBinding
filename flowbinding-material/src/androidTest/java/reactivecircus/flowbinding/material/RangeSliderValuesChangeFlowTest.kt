package reactivecircus.flowbinding.material

import android.view.MotionEvent
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.android.material.slider.RangeSlider
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.motionEventAtPosition
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class RangeSliderValuesChangeFlowTest {

    @Test
    fun rangeSliderValuesChanges() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<List<Float>>(testScope)
            val rangeSlider = getViewById<RangeSlider>(R.id.rangeSlider)
            rangeSlider.valuesChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(listOf(0.0f))
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_DOWN, 0, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 100, 50))
            assertThat(recorder.takeValue())
                .isEqualTo(listOf(100.0f))
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 0, 50))
            assertThat(recorder.takeValue())
                .isEqualTo(listOf(0.0f))
            recorder.assertNoMoreValues()

            cancelTestScope()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 50, 50))
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun rangeSliderValuesChanges_programmatic() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<List<Float>>(testScope)
            val rangeSlider = getViewById<RangeSlider>(R.id.rangeSlider)
            rangeSlider.valuesChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(listOf(0.0f))
            recorder.assertNoMoreValues()

            runOnUiThread {
                rangeSlider.values = listOf(50.0f)
            }
            assertThat(recorder.takeValue())
                .isEqualTo(listOf(50.0f))
            recorder.assertNoMoreValues()

            runOnUiThread {
                rangeSlider.values = listOf(75.0f)
            }
            assertThat(recorder.takeValue())
                .isEqualTo(listOf(75.0f))
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                rangeSlider.values = listOf(100.0f)
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun rangeSliderValuesChanges_skipInitialValue() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<List<Float>>(testScope)
            val rangeSlider = getViewById<RangeSlider>(R.id.rangeSlider)
            rangeSlider.valuesChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                rangeSlider.values = listOf(50.0f)
            }
            assertThat(recorder.takeValue())
                .isEqualTo(listOf(50.0f))
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                rangeSlider.values = listOf(100.0f)
            }
            recorder.assertNoMoreValues()
        }
    }
}
