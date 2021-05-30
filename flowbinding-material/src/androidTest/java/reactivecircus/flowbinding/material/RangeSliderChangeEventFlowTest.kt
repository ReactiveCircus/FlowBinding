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
class RangeSliderChangeEventFlowTest {

    @Test
    fun rangeSliderChangeEvents() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<RangeSliderChangeEvent>(testScope)
            val rangeSlider = getViewById<RangeSlider>(R.id.rangeSlider)
            rangeSlider.changeEvents().recordWith(recorder)

            val rangeSliderChangeEvent1 = recorder.takeValue()
            assertThat(rangeSliderChangeEvent1.rangeSlider)
                .isEqualTo(rangeSlider)
            assertThat(rangeSliderChangeEvent1.values)
                .isEqualTo(listOf(0.0f))
            assertThat(rangeSliderChangeEvent1.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_DOWN, 0, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 100, 50))
            val rangeSliderChangeEvent2 = recorder.takeValue()
            assertThat(rangeSliderChangeEvent2.rangeSlider)
                .isEqualTo(rangeSlider)
            assertThat(rangeSliderChangeEvent2.values)
                .isEqualTo(listOf(100.0f))
            assertThat(rangeSliderChangeEvent2.fromUser)
                .isTrue()
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(rangeSlider, MotionEvent.ACTION_MOVE, 0, 50))
            val rangeSliderChangeEvent3 = recorder.takeValue()
            assertThat(rangeSliderChangeEvent3.rangeSlider)
                .isEqualTo(rangeSlider)
            assertThat(rangeSliderChangeEvent3.values)
                .isEqualTo(listOf(0.0f))
            assertThat(rangeSliderChangeEvent3.fromUser)
                .isTrue()
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

            val rangeSliderChangeEvent1 = recorder.takeValue()

            assertThat(rangeSliderChangeEvent1.rangeSlider)
                .isEqualTo(rangeSlider)
            assertThat(rangeSliderChangeEvent1.values)
                .isEqualTo(listOf(0.0f))
            assertThat(rangeSliderChangeEvent1.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            runOnUiThread {
                rangeSlider.values = listOf(50.0f)
            }
            val rangeSliderChangeEvent2 = recorder.takeValue()
            assertThat(rangeSliderChangeEvent2.rangeSlider)
                .isEqualTo(rangeSlider)
            assertThat(rangeSliderChangeEvent2.values)
                .isEqualTo(listOf(50.0f))
            assertThat(rangeSliderChangeEvent2.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            runOnUiThread {
                rangeSlider.values = listOf(75.0f)
            }
            val rangeSliderChangeEvent3 = recorder.takeValue()
            assertThat(rangeSliderChangeEvent3.rangeSlider)
                .isEqualTo(rangeSlider)
            assertThat(rangeSliderChangeEvent3.values)
                .isEqualTo(listOf(75.0f))
            assertThat(rangeSliderChangeEvent3.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                rangeSlider.values = listOf(100.0f)
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun rangeSliderChangeEvents_skipInitialValue() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<RangeSliderChangeEvent>(testScope)
            val rangeSlider = getViewById<RangeSlider>(R.id.rangeSlider)
            rangeSlider.changeEvents()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                rangeSlider.values = listOf(50.0f)
            }
            val rangeSliderChangeEvent2 = recorder.takeValue()
            assertThat(rangeSliderChangeEvent2.rangeSlider)
                .isEqualTo(rangeSlider)
            assertThat(rangeSliderChangeEvent2.values)
                .isEqualTo(listOf(50.0f))
            assertThat(rangeSliderChangeEvent2.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                rangeSlider.values = listOf(100.0f)
            }
            recorder.assertNoMoreValues()
        }
    }
}
