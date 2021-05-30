package reactivecircus.flowbinding.android.widget

import android.view.MotionEvent
import android.widget.SeekBar
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.motionEventAtPosition
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SeekBarChangeEventFlowTest {

    @Test
    fun seekBarChangeEvents() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<SeekBarChangeEvent>(testScope)
            val seekBar = getViewById<SeekBar>(R.id.seekBar)
            seekBar.changeEvents().recordWith(recorder)

            val initialEvent = recorder.takeValue() as SeekBarChangeEvent.ProgressChanged
            assertThat(initialEvent.view)
                .isEqualTo(seekBar)
            assertThat(initialEvent.progress)
                .isEqualTo(0)
            assertThat(initialEvent.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_DOWN, 0, 50))
            val startEvent = recorder.takeValue() as SeekBarChangeEvent.StartTracking
            assertThat(startEvent.view)
                .isEqualTo(seekBar)
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_MOVE, 100, 50))
            val progressEvent = recorder.takeValue() as SeekBarChangeEvent.ProgressChanged
            assertThat(progressEvent.view)
                .isEqualTo(seekBar)
            assertThat(progressEvent.progress)
                .isEqualTo(100)
            assertThat(progressEvent.fromUser)
                .isTrue()
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_UP, 100, 50))
            val stopEvent = recorder.takeValue() as SeekBarChangeEvent.StopTracking
            assertThat(stopEvent.view)
                .isEqualTo(seekBar)
            recorder.assertNoMoreValues()

            cancelTestScope()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_MOVE, 100, 50))
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun seekBarChangeEvents_programmatic() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<SeekBarChangeEvent>(testScope)
            val seekBar = getViewById<SeekBar>(R.id.seekBar)
            seekBar.changeEvents().recordWith(recorder)

            val initialEvent = recorder.takeValue() as SeekBarChangeEvent.ProgressChanged
            assertThat(initialEvent.view)
                .isEqualTo(seekBar)
            assertThat(initialEvent.progress)
                .isEqualTo(0)
            assertThat(initialEvent.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            seekBar.progress = 50
            val event1 = recorder.takeValue() as SeekBarChangeEvent.ProgressChanged
            assertThat(event1.view)
                .isEqualTo(seekBar)
            assertThat(event1.progress)
                .isEqualTo(50)
            assertThat(event1.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            seekBar.progress = 75
            val event2 = recorder.takeValue() as SeekBarChangeEvent.ProgressChanged
            assertThat(event2.view)
                .isEqualTo(seekBar)
            assertThat(event2.progress)
                .isEqualTo(75)
            assertThat(event2.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            seekBar.progress = 100
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun seekBarChangeEvents_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<SeekBarChangeEvent>(testScope)
            val seekBar = getViewById<SeekBar>(R.id.seekBar)
            seekBar.changeEvents()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            seekBar.progress = 50
            val event = recorder.takeValue() as SeekBarChangeEvent.ProgressChanged
            assertThat(event.view)
                .isEqualTo(seekBar)
            assertThat(event.progress)
                .isEqualTo(50)
            assertThat(event.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            seekBar.progress = 100
            recorder.assertNoMoreValues()
        }
    }
}
