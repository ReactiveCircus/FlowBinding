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
class SeekBarProgressChangeFlowTest {

    @Test
    fun seekBarProgressChanges() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val seekBar = getViewById<SeekBar>(R.id.seekBar)
            seekBar.progressChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_DOWN, 0, 50))
            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_MOVE, 100, 50))
            recorder.takeValue() shouldEqual 100
            recorder.assertNoMoreValues()

            getInstrumentation().sendPointerSync(motionEventAtPosition(seekBar, MotionEvent.ACTION_MOVE, 0, 50))
            recorder.takeValue() shouldEqual 0
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

            recorder.assertNoMoreValues()

            seekBar.progress = 50
            recorder.takeValue() shouldEqual 50

            seekBar.progress = 75
            recorder.takeValue() shouldEqual 75
            recorder.assertNoMoreValues()

            cancelTestScope()

            seekBar.progress = 100
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun seekBarProgressChanges_emitImmediately() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<SeekBarChangeEvent>(testScope)
            val seekBar = getViewById<SeekBar>(R.id.seekBar)
            seekBar.changeEvents(emitImmediately = true).recordWith(recorder)

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

            cancelTestScope()

            seekBar.progress = 100
            recorder.assertNoMoreValues()
        }
    }
}
