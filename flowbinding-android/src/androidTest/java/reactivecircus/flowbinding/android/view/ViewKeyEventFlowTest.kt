package reactivecircus.flowbinding.android.view

import android.view.KeyEvent
import android.view.View
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewKeyEventFlowTest {

    @Test
    fun viewKeys() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<KeyEvent>(testScope)
            val view = View(rootView.context)
            view.keys().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                view.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            val event1 = recorder.takeValue()
            assertThat(event1.action)
                .isEqualTo(KeyEvent.ACTION_DOWN)
            assertThat(event1.keyCode)
                .isEqualTo(KeyEvent.KEYCODE_ENTER)

            runOnUiThread {
                view.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_A))
            }
            val event2 = recorder.takeValue()
            assertThat(event2.action)
                .isEqualTo(KeyEvent.ACTION_UP)
            assertThat(event2.keyCode)
                .isEqualTo(KeyEvent.KEYCODE_A)

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
            val view = View(rootView.context)
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
