package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.button.MaterialButtonToggleGroup
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickView
import reactivecircus.flowbinding.material.fixtures.MaterialFragment
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MaterialButtonToggleGroupButtonCheckedChangedFlowTest {

    @Test
    fun materialButtonToggleGroupButtonCheckedChanges_click() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<MaterialButtonCheckedChangedEvent>(testScope)
            getViewById<MaterialButtonToggleGroup>(R.id.buttonToggleGroup).buttonCheckedChanges()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            clickView(R.id.button1)
            val event1 = recorder.takeValue()
            event1.checked shouldEqual true
            event1.checkedId shouldEqual R.id.button1
            recorder.assertNoMoreValues()

            clickView(R.id.button2)
            val event2 = recorder.takeValue()
            event2.checked shouldEqual true
            event2.checkedId shouldEqual R.id.button2
            val event3 = recorder.takeValue()
            event3.checked shouldEqual false
            event3.checkedId shouldEqual R.id.button1
            recorder.assertNoMoreValues()

            clickView(R.id.button2)
            val event4 = recorder.takeValue()
            event4.checked shouldEqual false
            event4.checkedId shouldEqual R.id.button2
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(R.id.button3)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialButtonToggleGroupButtonCheckedChanges_programmatic() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<MaterialButtonCheckedChangedEvent>(testScope)
            val buttonGroup = getViewById<MaterialButtonToggleGroup>(R.id.buttonToggleGroup)
            buttonGroup.buttonCheckedChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { buttonGroup.check(R.id.button1) }
            val event1 = recorder.takeValue()
            event1.checked shouldEqual true
            event1.checkedId shouldEqual R.id.button1
            recorder.assertNoMoreValues()

            runOnUiThread { buttonGroup.uncheck(R.id.button1) }
            val event2 = recorder.takeValue()
            event2.checked shouldEqual false
            event2.checkedId shouldEqual R.id.button1
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { buttonGroup.check(R.id.button3) }
            recorder.assertNoMoreValues()
        }
    }
}
