package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.textfield.TextInputLayout
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.longClickTextInputLayoutIcon
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.longClickTextInputLayoutErrorIcon
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class TextInputLayoutIconLongClickedFlowTest {

    @Test
    fun textInputLayoutStartIconLongClicks() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Unit>(testScope)
            val textInputLayout = getViewById<TextInputLayout>(R.id.textInputLayout)
            textInputLayout.startIconLongClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            longClickTextInputLayoutIcon(R.id.textInputLayout, endIcon = false)
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            longClickTextInputLayoutIcon(R.id.textInputLayout, endIcon = false)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textInputLayoutEndIconLongClicks() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Unit>(testScope)
            val textInputLayout = getViewById<TextInputLayout>(R.id.textInputLayout)
            textInputLayout.endIconLongClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            longClickTextInputLayoutIcon(R.id.textInputLayout, endIcon = true)
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            longClickTextInputLayoutIcon(R.id.textInputLayout, endIcon = true)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun textInputLayoutErrorIconLongClicks() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Unit>(testScope)
            val textInputLayout = getViewById<TextInputLayout>(R.id.textInputLayout).apply {
                runOnUiThread {
                    error = "Error"
                    setErrorIconOnClickListener {}
                }
            }
            textInputLayout.errorIconLongClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            longClickTextInputLayoutErrorIcon(R.id.textInputLayout)
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            longClickTextInputLayoutErrorIcon(R.id.textInputLayout)
            recorder.assertNoMoreValues()
        }
    }
}
