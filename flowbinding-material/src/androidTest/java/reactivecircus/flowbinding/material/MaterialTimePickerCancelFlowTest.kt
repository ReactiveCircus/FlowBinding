package reactivecircus.flowbinding.material

import androidx.fragment.app.FragmentActivity
import androidx.test.filters.LargeTest
import com.google.android.material.timepicker.MaterialTimePicker
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.pressBack
import reactivecircus.blueprint.testing.currentActivity
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickCancelButtonOnTimePicker
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MaterialTimePickerCancelFlowTest {

    @Test
    fun materialTimePickerCancels_pressBackButton() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialTimePicker.Builder().build()
            picker.cancels().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialTimePickerCancels_notEmitWhenClickedNegativeButton() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialTimePicker.Builder().build()
            picker.cancels().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickCancelButtonOnTimePicker()
            recorder.assertNoMoreValues()

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialTimePickerCancels_notEmitWhenDismissedProgrammatically() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialTimePicker.Builder().build()
            picker.cancels().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            picker.dismiss()
            recorder.assertNoMoreValues()

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            recorder.assertNoMoreValues()
        }
    }
}
