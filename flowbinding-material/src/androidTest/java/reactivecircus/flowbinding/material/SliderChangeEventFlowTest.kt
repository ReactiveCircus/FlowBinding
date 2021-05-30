package reactivecircus.flowbinding.material

import android.view.MotionEvent
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.android.material.slider.Slider
import com.google.common.truth.Truth.assertThat
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

            val sliderChangeEvent1 = recorder.takeValue()
            assertThat(sliderChangeEvent1.slider)
                .isEqualTo(slider)
            assertThat(sliderChangeEvent1.value)
                .isEqualTo(0f)
            assertThat(sliderChangeEvent1.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_DOWN, 0, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
            val sliderChangeEvent2 = recorder.takeValue()
            assertThat(sliderChangeEvent2.slider)
                .isEqualTo(slider)
            assertThat(sliderChangeEvent2.value)
                .isEqualTo(1f)
            assertThat(sliderChangeEvent2.fromUser)
                .isTrue()
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 0, 50))
            val sliderChangeEvent3 = recorder.takeValue()
            assertThat(sliderChangeEvent3.slider)
                .isEqualTo(slider)
            assertThat(sliderChangeEvent3.value)
                .isEqualTo(0f)
            assertThat(sliderChangeEvent3.fromUser)
                .isTrue()
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

            val sliderChangeEvent1 = recorder.takeValue()
            assertThat(sliderChangeEvent1.slider)
                .isEqualTo(slider)
            assertThat(sliderChangeEvent1.value)
                .isEqualTo(0f)
            assertThat(sliderChangeEvent1.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            runOnUiThread {
                slider.value = 0.5f
            }
            val sliderChangeEvent2 = recorder.takeValue()
            assertThat(sliderChangeEvent2.slider)
                .isEqualTo(slider)
            assertThat(sliderChangeEvent2.value)
                .isEqualTo(0.5f)
            assertThat(sliderChangeEvent2.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            runOnUiThread {
                slider.value = 0.75f
            }
            val sliderChangeEvent3 = recorder.takeValue()
            assertThat(sliderChangeEvent3.slider)
                .isEqualTo(slider)
            assertThat(sliderChangeEvent3.value)
                .isEqualTo(0.75f)
            assertThat(sliderChangeEvent3.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                slider.value = 1f
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun sliderChangeEvents_skipInitialValue() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<SliderChangeEvent>(testScope)
            val slider = getViewById<Slider>(R.id.slider)
            slider.changeEvents()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                slider.value = 0.5f
            }
            val sliderChangeEvent = recorder.takeValue()
            assertThat(sliderChangeEvent.slider)
                .isEqualTo(slider)
            assertThat(sliderChangeEvent.value)
                .isEqualTo(0.5f)
            assertThat(sliderChangeEvent.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                slider.value = 1f
            }
            recorder.assertNoMoreValues()
        }
    }
}
