package reactivecircus.flowbinding.android.widget

import android.widget.RadioGroup
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickView
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class RadioGroupCheckedChangedFlowTest {

    @Test
    fun radioGroupCheckedChanges_click() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val radioGroup = getViewById<RadioGroup>(R.id.radioGroup).apply {
                runOnUiThread { check(R.id.radioButton1) }
            }
            radioGroup.checkedChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(R.id.radioButton1)
            recorder.assertNoMoreValues()

            clickView(R.id.radioButton2)
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.radioButton2)
            recorder.assertNoMoreValues()

            clickView(R.id.radioButton3)
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.radioButton3)
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(R.id.radioButton1)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun radioGroupCheckedChanges_programmatic() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val radioGroup = getViewById<RadioGroup>(R.id.radioGroup).apply {
                runOnUiThread { check(R.id.radioButton1) }
            }
            radioGroup.checkedChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(R.id.radioButton1)
            recorder.assertNoMoreValues()

            runOnUiThread { radioGroup.check(R.id.radioButton2) }
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.radioButton2)
            recorder.assertNoMoreValues()

            runOnUiThread { radioGroup.check(R.id.radioButton1) }
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.radioButton1)

            cancelTestScope()

            runOnUiThread { radioGroup.check(R.id.radioButton3) }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun radioGroupCheckedChanges_unchecked() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val radioGroup = getViewById<RadioGroup>(R.id.radioGroup)
            radioGroup.checkedChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(-1)
            recorder.assertNoMoreValues()

            clickView(R.id.radioButton1)
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.radioButton1)
            recorder.assertNoMoreValues()

            runOnUiThread {
                radioGroup.clearCheck()
            }

            assertThat(recorder.takeValue())
                .isEqualTo(-1)
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(R.id.radioButton1)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun radioGroupCheckedChanges_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val radioGroup = getViewById<RadioGroup>(R.id.radioGroup).apply {
                runOnUiThread { check(R.id.radioButton1) }
            }
            radioGroup.checkedChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { radioGroup.check(R.id.radioButton2) }
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.radioButton2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { radioGroup.check(R.id.radioButton3) }
            recorder.assertNoMoreValues()
        }
    }
}
