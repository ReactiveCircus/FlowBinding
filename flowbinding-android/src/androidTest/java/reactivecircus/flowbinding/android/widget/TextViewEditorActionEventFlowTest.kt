package reactivecircus.flowbinding.android.widget

import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_GO
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.widget.TextView
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class TextViewEditorActionEventFlowTest {

    @Test
    fun textViewEditorActionChangeEvents() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<EditorActionEvent>(testScope)
            val textView = TextView(rootView.context)
            textView.editorActionEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            textView.onEditorAction(IME_ACTION_GO)
            val event1 = recorder.takeValue()
            assertThat(event1.view)
                .isEqualTo(textView)
            assertThat(event1.actionId)
                .isEqualTo(IME_ACTION_GO)
            assertThat(event1.keyEvent)
                .isNull()
            recorder.assertNoMoreValues()

            textView.onEditorAction(IME_ACTION_NEXT)
            val event2 = recorder.takeValue()
            assertThat(event2.view)
                .isEqualTo(textView)
            assertThat(event2.actionId)
                .isEqualTo(IME_ACTION_NEXT)
            assertThat(event2.keyEvent)
                .isNull()
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
            val textView = TextView(rootView.context)
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
