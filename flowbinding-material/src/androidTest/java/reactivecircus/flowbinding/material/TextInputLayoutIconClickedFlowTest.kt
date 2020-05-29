package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.textfield.TextInputLayout
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickTextInputLayoutIcon
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickTextInputLayoutErrorIcon
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class TextInputLayoutIconClickedFlowTest {

    @Test
    fun textInputLayoutStartIconClicks() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Unit>(testScope)
            val textInputLayout = getViewById<TextInputLayout>(R.id.textInputLayout)
            textInputLayout.startIconClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            clickTextInputLayoutIcon(R.id.textInputLayout, endIcon = false)
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickTextInputLayoutIcon(R.id.textInputLayout, endIcon = false)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textInputLayoutEndIconClicks() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Unit>(testScope)
            val textInputLayout = getViewById<TextInputLayout>(R.id.textInputLayout)
            textInputLayout.endIconClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            clickTextInputLayoutIcon(R.id.textInputLayout, endIcon = true)
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickTextInputLayoutIcon(R.id.textInputLayout, endIcon = true)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textInputLayoutErrorIconClicks() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Unit>(testScope)
            val textInputLayout = getViewById<TextInputLayout>(R.id.textInputLayout).apply {
                runOnUiThread { error = "Error" }
            }
            textInputLayout.errorIconClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            clickTextInputLayoutErrorIcon(R.id.textInputLayout)
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickTextInputLayoutErrorIcon(R.id.textInputLayout)
            recorder.assertNoMoreValues()
        }
    }
}
