package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import com.google.android.material.textfield.TextInputLayout
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.longClickTextInputLayoutIcon
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
}
