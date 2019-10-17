package reactivecircus.flowbinding.android.view

import android.view.KeyEvent
import android.view.View
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewKeyEventFlowTest {

    @Test
    fun viewKeys() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<KeyEvent>(testScope)
            val view = getViewById<View>(R.id.editText1)
            view.keys().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                view.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            val event1 = recorder.takeValue()
            event1.action shouldEqual KeyEvent.ACTION_DOWN
            event1.keyCode shouldEqual KeyEvent.KEYCODE_ENTER

            runOnUiThread {
                view.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_A))
            }
            val event2 = recorder.takeValue()
            event2.action shouldEqual KeyEvent.ACTION_UP
            event2.keyCode shouldEqual KeyEvent.KEYCODE_A

            cancelTestScope()

            runOnUiThread {
                view.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun viewKeys_notHandled() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<KeyEvent>(testScope)
            val view = getViewById<View>(R.id.editText1)
            view.keys { false }
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                view.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                view.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            recorder.assertNoMoreValues()
        }
    }
}
