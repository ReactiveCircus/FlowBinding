package reactivecircus.flowbinding.material

import androidx.fragment.app.FragmentActivity
import androidx.test.filters.LargeTest
import com.google.android.material.datepicker.MaterialDatePicker
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.pressBack
import reactivecircus.blueprint.testing.currentActivity
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickCancelButtonOnDatePicker
import reactivecircus.flowbinding.testing.clickOkButtonOnDatePicker
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MaterialDatePickerDismissFlowTest {

    @Test
    fun materialDatePickerDismisses_pressBackButton() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialDatePicker.Builder.datePicker().build()
            picker.dismisses().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            recorder.takeValue() shouldEqual Unit

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialDatePickerDismisses_clickPositiveButton() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(System.currentTimeMillis())
                .build()
            picker.dismisses().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickOkButtonOnDatePicker()
            recorder.takeValue() shouldEqual Unit

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickOkButtonOnDatePicker()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialDatePickerDismisses_clickNegativeButton() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(System.currentTimeMillis())
                .build()
            picker.dismisses().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickCancelButtonOnDatePicker()
            recorder.takeValue() shouldEqual Unit

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickCancelButtonOnDatePicker()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialDatePickerDismisses_programmatic() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialDatePicker.Builder.datePicker().build()
            picker.dismisses().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            picker.dismiss()
            recorder.takeValue() shouldEqual Unit

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            picker.dismiss()
            recorder.assertNoMoreValues()
        }
    }
}
