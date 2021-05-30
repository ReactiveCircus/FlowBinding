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
class TextViewTextChangeFlowTest {

    @Test
    fun textViewTextChanges() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<CharSequence>(testScope)
            val textView = TextView(rootView.context).apply {
                text = "ABC"
            }
            textView.textChanges().recordWith(recorder)

            assertThat(recorder.takeValue().toString())
                .isEqualTo("ABC")
            recorder.assertNoMoreValues()

            textView.text = "A"
            assertThat(recorder.takeValue().toString())
                .isEqualTo("A")
            recorder.assertNoMoreValues()

            textView.text = "AB"
            assertThat(recorder.takeValue().toString())
                .isEqualTo("AB")
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textViewTextChanges_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<CharSequence>(testScope)
            val textView = TextView(rootView.context).apply {
                text = "ABC"
            }
            textView.textChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            textView.text = "A"
            assertThat(recorder.takeValue().toString())
                .isEqualTo("A")
            recorder.assertNoMoreValues()

            textView.text = "AB"
            assertThat(recorder.takeValue().toString())
                .isEqualTo("AB")
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }
}
