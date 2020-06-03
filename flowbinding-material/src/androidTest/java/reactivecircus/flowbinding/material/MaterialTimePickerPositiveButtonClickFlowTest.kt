package reactivecircus.flowbinding.material

import androidx.fragment.app.FragmentActivity
import androidx.test.filters.LargeTest
import com.google.android.material.timepicker.MaterialTimePicker
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.currentActivity
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickOkButtonOnTimePicker
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MaterialTimePickerPositiveButtonClickFlowTest {

    @Test
    fun materialTimePickerPositiveButtonClicks() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialTimePicker.Builder().build()
            picker.positiveButtonClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickOkButtonOnTimePicker()
            recorder.takeValue() shouldEqual Unit

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickOkButtonOnTimePicker()
            recorder.assertNoMoreValues()
        }
    }
}
