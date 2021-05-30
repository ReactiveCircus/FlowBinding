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
class TextViewAfterTextChangeEventFlowTest {

    @Test
    fun textViewAfterTextChangeEvents() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<AfterTextChangeEvent>(testScope)
            val textView = TextView(rootView.context)
            textView.afterTextChanges().recordWith(recorder)

            val initialEvent = recorder.takeValue()
            assertThat(initialEvent.view)
                .isEqualTo(textView)
            assertThat(initialEvent.editable)
                .isNull()
            recorder.assertNoMoreValues()

            textView.text = "A"
            val event1 = recorder.takeValue()
            assertThat(event1.view)
                .isEqualTo(textView)
            assertThat(event1.editable.toString())
                .isEqualTo("A")
            recorder.assertNoMoreValues()

            textView.text = "B"
            val event2 = recorder.takeValue()
            assertThat(event2.view)
                .isEqualTo(textView)
            assertThat(event2.editable.toString())
                .isEqualTo("B")
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textViewAfterTextChangeEvents_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<AfterTextChangeEvent>(testScope)
            val textView = TextView(rootView.context)
            textView.afterTextChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            textView.text = "A"
            val event1 = recorder.takeValue()
            assertThat(event1.view)
                .isEqualTo(textView)
            assertThat(event1.editable.toString())
                .isEqualTo("A")
            recorder.assertNoMoreValues()

            textView.text = "B"
            val event2 = recorder.takeValue()
            assertThat(event2.view)
                .isEqualTo(textView)
            assertThat(event2.editable.toString())
                .isEqualTo("B")
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }
}
