package reactivecircus.flowbinding.android.widget

import android.widget.CompoundButton
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickView
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class CompoundButtonCheckedChangedFlowTest {

    @Test
    fun compoundButtonCheckedChanges_click() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Boolean>(testScope)
            getViewById<CompoundButton>(R.id.toggleButton)
                .checkedChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            clickView(R.id.toggleButton)
            recorder.takeValue() shouldEqual true
            recorder.assertNoMoreValues()

            clickView(R.id.toggleButton)
            recorder.takeValue() shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(R.id.toggleButton)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun compoundButtonCheckedChanges_programmatic() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val button = getViewById<CompoundButton>(R.id.toggleButton)
            button.checkedChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { button.isChecked = true }
            recorder.takeValue() shouldEqual true
            recorder.assertNoMoreValues()

            runOnUiThread { button.isChecked = false }
            recorder.takeValue() shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { button.isChecked = true }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun compoundButtonCheckedChanges_emitImmediately() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val button = getViewById<CompoundButton>(R.id.toggleButton).apply {
                runOnUiThread { isChecked = false }
            }
            button.checkedChanges(emitImmediately = true).recordWith(recorder)

            recorder.takeValue() shouldEqual false
            recorder.assertNoMoreValues()

            runOnUiThread { button.isChecked = true }
            recorder.takeValue() shouldEqual true
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { button.isChecked = false }
            recorder.assertNoMoreValues()
        }
    }
}
