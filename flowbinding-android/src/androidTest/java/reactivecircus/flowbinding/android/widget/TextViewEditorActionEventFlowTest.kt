package reactivecircus.flowbinding.android.widget

import android.content.Context
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_GO
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class TextViewEditorActionEventFlowTest {

    private val appContext = ApplicationProvider.getApplicationContext<Context>().applicationContext

    @Test
    fun textViewEditorActionChangeEvents() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<EditorActionEvent>(testScope)
            val textView = TextView(appContext)
            textView.editorActionEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            textView.onEditorAction(IME_ACTION_GO)
            val event1 = recorder.takeValue()
            event1.view shouldEqual textView
            event1.actionId shouldEqual IME_ACTION_GO
            event1.keyEvent shouldEqual null
            recorder.assertNoMoreValues()

            textView.onEditorAction(IME_ACTION_NEXT)
            val event2 = recorder.takeValue()
            event2.view shouldEqual textView
            event2.actionId shouldEqual IME_ACTION_NEXT
            event2.keyEvent shouldEqual null
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.onEditorAction(IME_ACTION_DONE)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textViewEditorActionChangeEvents_notHandled() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<EditorActionEvent>(testScope)
            val textView = TextView(appContext)
            textView.editorActionEvents { false }.recordWith(recorder)

            recorder.assertNoMoreValues()

            textView.onEditorAction(IME_ACTION_GO)
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.onEditorAction(IME_ACTION_NEXT)
            recorder.assertNoMoreValues()
        }
    }
}
