package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.textfield.TextInputLayout
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickTextInputLayoutEndIcon
import reactivecircus.blueprint.testing.action.clickTextInputLayoutErrorIcon
import reactivecircus.blueprint.testing.action.clickTextInputLayoutStartIcon
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
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

            clickTextInputLayoutStartIcon(R.id.textInputLayout)
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickTextInputLayoutStartIcon(R.id.textInputLayout)
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

            clickTextInputLayoutEndIcon(R.id.textInputLayout)
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickTextInputLayoutEndIcon(R.id.textInputLayout)
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
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickTextInputLayoutErrorIcon(R.id.textInputLayout)
            recorder.assertNoMoreValues()
        }
    }
}
