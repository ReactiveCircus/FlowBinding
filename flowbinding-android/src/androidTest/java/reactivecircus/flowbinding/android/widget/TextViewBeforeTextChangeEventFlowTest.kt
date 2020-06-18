package reactivecircus.flowbinding.android.widget

import android.widget.TextView
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
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
            initialEvent.view shouldEqual textView
            initialEvent.text.toString() shouldEqual "ABC"
            initialEvent.start shouldEqual 0
            initialEvent.count shouldEqual 0
            initialEvent.after shouldEqual 0
            recorder.assertNoMoreValues()

            textView.text = "A"
            val event1 = recorder.takeValue()
            event1.view shouldEqual textView
            event1.text.toString() shouldEqual "ABC"
            event1.start shouldEqual 0
            event1.count shouldEqual 3
            event1.after shouldEqual 1
            recorder.assertNoMoreValues()

            textView.text = "AB"
            val event2 = recorder.takeValue()
            event2.view shouldEqual textView
            event2.text.toString() shouldEqual "A"
            event2.start shouldEqual 0
            event2.count shouldEqual 1
            event2.after shouldEqual 2
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
            event1.view shouldEqual textView
            event1.text.toString() shouldEqual "ABC"
            event1.start shouldEqual 0
            event1.count shouldEqual 3
            event1.after shouldEqual 1
            recorder.assertNoMoreValues()

            textView.text = "AB"
            val event2 = recorder.takeValue()
            event2.view shouldEqual textView
            event2.text.toString() shouldEqual "A"
            event2.start shouldEqual 0
            event2.count shouldEqual 1
            event2.after shouldEqual 2
            recorder.assertNoMoreValues()

            cancelTestScope()

            textView.text = "C"
            recorder.assertNoMoreValues()
        }
    }
}
