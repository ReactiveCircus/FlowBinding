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
class SeekBarProgressChangeFlowTest {

    @Test
    fun seekBarProgressChanges() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val seekBar = getViewById<SeekBar>(R.id.seekBar)
            seekBar.progressChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_DOWN, 0, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_MOVE, 100, 50))
            assertThat(recorder.takeValue())
                .isEqualTo(100)
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_MOVE, 0, 50))
            assertThat(recorder.takeValue())
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            cancelTestScope()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_MOVE, 50, 50))
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun seekBarProgressChanges_programmatic() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val seekBar = getViewById<SeekBar>(R.id.seekBar)
            seekBar.progressChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            seekBar.progress = 50
            assertThat(recorder.takeValue())
                .isEqualTo(50)

            seekBar.progress = 75
            assertThat(recorder.takeValue())
                .isEqualTo(75)
            recorder.assertNoMoreValues()

            cancelTestScope()

            seekBar.progress = 100
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun seekBarProgressChanges_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val seekBar = getViewById<SeekBar>(R.id.seekBar)
            seekBar.progressChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            seekBar.progress = 50
            assertThat(recorder.takeValue())
                .isEqualTo(50)
            recorder.assertNoMoreValues()

            cancelTestScope()

            seekBar.progress = 100
            recorder.assertNoMoreValues()
        }
    }
}
