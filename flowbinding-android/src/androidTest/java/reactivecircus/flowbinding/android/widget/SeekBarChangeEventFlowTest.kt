package reactivecircus.flowbinding.android.widget

import android.view.MotionEvent
import android.widget.SeekBar
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.amshove.kluent.shouldEqual
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
            initialEvent.view shouldEqual seekBar
            initialEvent.progress shouldEqual 0
            initialEvent.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_DOWN, 0, 50))
            val startEvent = recorder.takeValue() as SeekBarChangeEvent.StartTracking
            startEvent.view shouldEqual seekBar
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_MOVE, 100, 50))
            val progressEvent = recorder.takeValue() as SeekBarChangeEvent.ProgressChanged
            progressEvent.view shouldEqual seekBar
            progressEvent.progress shouldEqual 100
            progressEvent.fromUser shouldEqual true
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_UP, 100, 50))
            val stopEvent = recorder.takeValue() as SeekBarChangeEvent.StopTracking
            stopEvent.view shouldEqual seekBar
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
            initialEvent.view shouldEqual seekBar
            initialEvent.progress shouldEqual 0
            initialEvent.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            seekBar.progress = 50
            val event1 = recorder.takeValue() as SeekBarChangeEvent.ProgressChanged
            event1.view shouldEqual seekBar
            event1.progress shouldEqual 50
            event1.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            seekBar.progress = 75
            val event2 = recorder.takeValue() as SeekBarChangeEvent.ProgressChanged
            event2.view shouldEqual seekBar
            event2.progress shouldEqual 75
            event2.fromUser shouldEqual false
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
            event.view shouldEqual seekBar
            event.progress shouldEqual 50
            event.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            seekBar.progress = 100
            recorder.assertNoMoreValues()
        }
    }
}
