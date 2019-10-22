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
class TextViewAfterTextChangeEventFlowTest {

    private val appContext = ApplicationProvider.getApplicationContext<Context>().applicationContext

    @Test
    fun textViewAfterTextChangeEvents() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<AfterTextChangeEvent>(testScope)
            val textView = TextView(appContext)
            textView.afterTextChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            textView.text = "A"
            val event1 = recorder.takeValue()
            event1.view shouldEqual textView
            event1.editable.toString() shouldEqual "A"
            recorder.assertNoMoreValues()

            textView.text = "B"
            val event2 = recorder.takeValue()
            event2.view shouldEqual textView
            event2.editable.toString() shouldEqual "B"
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textViewAfterTextChangeEvents_emitImmediately() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<AfterTextChangeEvent>(testScope)
            val textView = TextView(appContext)
            textView.afterTextChanges(emitImmediately = true).recordWith(recorder)

            val initialEvent = recorder.takeValue()
            initialEvent.view shouldEqual textView
            initialEvent.editable shouldEqual null
            recorder.assertNoMoreValues()

            textView.text = "A"
            val event1 = recorder.takeValue()
            event1.view shouldEqual textView
            event1.editable.toString() shouldEqual "A"
            recorder.assertNoMoreValues()

            textView.text = "B"
            val event2 = recorder.takeValue()
            event2.view shouldEqual textView
            event2.editable.toString() shouldEqual "B"
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }
}
