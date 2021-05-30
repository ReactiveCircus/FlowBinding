package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickView
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MaterialButtonToggleGroupCheckedChangedFlowTest {

    @Test
    fun materialButtonToggleGroupButtonCheckedChanges_click() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<MaterialButtonCheckedChangedEvent>(testScope)
            getViewById<MaterialButtonToggleGroup>(R.id.buttonToggleGroup).buttonCheckedChanges()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            clickView(R.id.button1)
            val event1 = recorder.takeValue()
            assertThat(event1.checked)
                .isTrue()
            assertThat(event1.checkedId)
                .isEqualTo(R.id.button1)
            recorder.assertNoMoreValues()

            clickView(R.id.button2)
            val event2 = recorder.takeValue()
            assertThat(event2.checked)
                .isFalse()
            assertThat(event2.checkedId)
                .isEqualTo(R.id.button1)

            val event3 = recorder.takeValue()
            assertThat(event3.checked)
                .isTrue()
            assertThat(event3.checkedId)
                .isEqualTo(R.id.button2)
            recorder.assertNoMoreValues()

            clickView(R.id.button2)
            val event4 = recorder.takeValue()
            assertThat(event4.checked)
                .isFalse()
            assertThat(event4.checkedId)
                .isEqualTo(R.id.button2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(R.id.button3)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialButtonToggleGroupButtonCheckedChanges_programmatic() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<MaterialButtonCheckedChangedEvent>(testScope)
            val buttonGroup = getViewById<MaterialButtonToggleGroup>(R.id.buttonToggleGroup)
            buttonGroup.buttonCheckedChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { buttonGroup.check(R.id.button1) }
            val event1 = recorder.takeValue()
            assertThat(event1.checked)
                .isTrue()
            assertThat(event1.checkedId)
                .isEqualTo(R.id.button1)
            recorder.assertNoMoreValues()

            runOnUiThread { buttonGroup.uncheck(R.id.button1) }
            val event2 = recorder.takeValue()
            assertThat(event2.checked)
                .isFalse()
            assertThat(event2.checkedId)
                .isEqualTo(R.id.button1)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { buttonGroup.check(R.id.button3) }
            recorder.assertNoMoreValues()
        }
    }
}
