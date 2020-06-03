package reactivecircus.flowbinding.material

import androidx.fragment.app.FragmentActivity
import androidx.test.filters.LargeTest
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.pressBack
import reactivecircus.blueprint.testing.currentActivity
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickCancelButtonOnTimePicker
import reactivecircus.flowbinding.testing.clickOkButtonOnTimePicker
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MaterialTimePickerDismissFlowTest {

    @Test
    fun materialTimePickerDismisses_pressBackButton() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialTimePicker.Builder().build()
            picker.dismisses().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialTimePickerDismisses_clickPositiveButton() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialTimePicker.Builder().build()
            picker.dismisses().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickOkButtonOnTimePicker()
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickOkButtonOnTimePicker()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialTimePickerDismisses_clickNegativeButton() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialTimePicker.Builder().build()
            picker.dismisses().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickCancelButtonOnTimePicker()
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickCancelButtonOnTimePicker()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialTimePickerDismisses_programmatic() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialTimePicker.Builder().build()
            picker.dismisses().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            picker.dismiss()
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            picker.dismiss()
            recorder.assertNoMoreValues()
        }
    }
}
