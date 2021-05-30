package reactivecircus.flowbinding.android.widget

import android.widget.TextView
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class TextViewBeforeTextChangeEventFlowTest {

    @Test
    fun textViewBeforeTextChangeEvents() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<BeforeTextChangeEvent>(testScope)
            val textView = TextView(rootView.context).apply {
                text = "ABC"
            }
            textView.beforeTextChanges().recordWith(recorder)

            val initialEvent = recorder.takeValue()
            assertThat(initialEvent.view)
                .isEqualTo(textView)
            assertThat(initialEvent.text.toString())
                .isEqualTo("ABC")
            assertThat(initialEvent.start)
                .isEqualTo(0)
            assertThat(initialEvent.count)
                .isEqualTo(0)
            assertThat(initialEvent.after)
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            textView.text = "A"
            val event1 = recorder.takeValue()
            assertThat(event1.view)
                .isEqualTo(textView)
            assertThat(event1.text.toString())
                .isEqualTo("ABC")
            assertThat(event1.start)
                .isEqualTo(0)
            assertThat(event1.count)
                .isEqualTo(3)
            assertThat(event1.after)
                .isEqualTo(1)
            recorder.assertNoMoreValues()

            textView.text = "AB"
            val event2 = recorder.takeValue()
            assertThat(event2.view)
                .isEqualTo(textView)
            assertThat(event2.text.toString())
                .isEqualTo("A")
            assertThat(event2.start)
                .isEqualTo(0)
            assertThat(event2.count)
                .isEqualTo(1)
            assertThat(event2.after)
                .isEqualTo(2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textViewBeforeTextChangeEvents_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<BeforeTextChangeEvent>(testScope)
            val textView = TextView(rootView.context).apply {
                text = "ABC"
            }
            textView.beforeTextChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            textView.text = "A"
            val event1 = recorder.takeValue()
            assertThat(event1.view)
                .isEqualTo(textView)
            assertThat(event1.text.toString())
                .isEqualTo("ABC")
            assertThat(event1.start)
                .isEqualTo(0)
            assertThat(event1.count)
                .isEqualTo(3)
            assertThat(event1.after)
                .isEqualTo(1)
            recorder.assertNoMoreValues()

            textView.text = "AB"
            val event2 = recorder.takeValue()
            assertThat(event2.view)
                .isEqualTo(textView)
            assertThat(event2.text.toString())
                .isEqualTo("A")
            assertThat(event2.start)
                .isEqualTo(0)
            assertThat(event2.count)
                .isEqualTo(1)
            assertThat(event2.after)
                .isEqualTo(2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }
}
