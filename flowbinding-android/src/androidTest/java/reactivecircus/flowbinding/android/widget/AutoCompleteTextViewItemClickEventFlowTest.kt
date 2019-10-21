package reactivecircus.flowbinding.android.widget

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe
import org.junit.Test
import reactivecircus.blueprint.testing.action.clearTextInView
import reactivecircus.blueprint.testing.action.enterTextIntoView
import reactivecircus.flowbinding.android.fixtures.widget.AutoCompleteViewFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickPopupItemAt
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AutoCompleteTextViewItemClickEventFlowTest {

    @Test
    fun autoCompleteTextViewItemClickEvents() {
        launchTest<AutoCompleteViewFragment> {
            val recorder = FlowRecorder<AdapterViewItemClickEvent>(testScope)
            val textView = getViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).apply {
                runOnUiThread {
                    val values = arrayOf("A", "AB", "ABC")
                    val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, values)
                    setAdapter(adapter)
                }
            }

            textView.itemClickEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            enterTextIntoView(R.id.autoCompleteTextView, "AB")
            clickPopupItemAt(1)
            val event = recorder.takeValue()
            event.view shouldNotBe null
            event.clickedView shouldNotBe null
            event.position shouldEqual 1
            event.id shouldEqual 1

            recorder.assertNoMoreValues()

            cancelTestScope()

            clearTextInView(R.id.autoCompleteTextView)
            enterTextIntoView(R.id.autoCompleteTextView, "AB")
            clickPopupItemAt(1)
            recorder.assertNoMoreValues()
        }
    }
}
