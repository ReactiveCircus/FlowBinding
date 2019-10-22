package reactivecircus.flowbinding.android.widget

import android.content.Context
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
class TextViewTextChangeFlowTest {

    private val appContext = ApplicationProvider.getApplicationContext<Context>().applicationContext

    @Test
    fun textViewTextChanges() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<CharSequence>(testScope)
            val textView = TextView(appContext).apply {
                text = "ABC"
            }
            textView.textChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            textView.text = "A"
            recorder.takeValue().toString() shouldEqual "A"
            recorder.assertNoMoreValues()

            textView.text = "AB"
            recorder.takeValue().toString() shouldEqual "AB"
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textViewTextChanges_emitImmediately() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<CharSequence>(testScope)
            val textView = TextView(appContext).apply {
                text = "ABC"
            }
            textView.textChanges(emitImmediately = true).recordWith(recorder)

            recorder.takeValue().toString() shouldEqual "ABC"
            recorder.assertNoMoreValues()

            textView.text = "A"
            recorder.takeValue().toString() shouldEqual "A"
            recorder.assertNoMoreValues()

            textView.text = "AB"
            recorder.takeValue().toString() shouldEqual "AB"
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }
}
