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
class TextViewTextChangeEventFlowTest {

    @Test
    fun textViewTextChangeEvents() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<TextChangeEvent>(testScope)
            val textView = TextView(rootView.context).apply {
                text = "ABC"
            }
            textView.textChangeEvents().recordWith(recorder)

            val initialEvent = recorder.takeValue()
            assertThat(initialEvent.view)
                .isEqualTo(textView)
            assertThat(initialEvent.text.toString())
                .isEqualTo("ABC")
            assertThat(initialEvent.start)
                .isEqualTo(0)
            assertThat(initialEvent.before)
                .isEqualTo(0)
            assertThat(initialEvent.count)
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            textView.text = "A"
            val event1 = recorder.takeValue()
            assertThat(event1.view)
                .isEqualTo(textView)
            assertThat(event1.text.toString())
                .isEqualTo("A")
            assertThat(event1.start)
                .isEqualTo(0)
            assertThat(event1.before)
                .isEqualTo(3)
            assertThat(event1.count)
                .isEqualTo(1)
            recorder.assertNoMoreValues()

            textView.text = "AB"
            val event2 = recorder.takeValue()
            assertThat(event2.view)
                .isEqualTo(textView)
            assertThat(event2.text.toString())
                .isEqualTo("AB")
            assertThat(event2.start)
                .isEqualTo(0)
            assertThat(event2.before)
                .isEqualTo(1)
            assertThat(event2.count)
                .isEqualTo(2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textViewTextChangeEvents_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<TextChangeEvent>(testScope)
            val textView = TextView(rootView.context).apply {
                text = "ABC"
            }
            textView.textChangeEvents()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            textView.text = "A"
            val event1 = recorder.takeValue()
            assertThat(event1.view)
                .isEqualTo(textView)
            assertThat(event1.text.toString())
                .isEqualTo("A")
            assertThat(event1.start)
                .isEqualTo(0)
            assertThat(event1.before)
                .isEqualTo(3)
            assertThat(event1.count)
                .isEqualTo(1)
            recorder.assertNoMoreValues()

            textView.text = "AB"
            val event2 = recorder.takeValue()
            assertThat(event2.view)
                .isEqualTo(textView)
            assertThat(event2.text.toString())
                .isEqualTo("AB")
            assertThat(event2.start)
                .isEqualTo(0)
            assertThat(event2.before)
                .isEqualTo(1)
            assertThat(event2.count)
                .isEqualTo(2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }
}
