package reactivecircus.flowbinding.material

import androidx.fragment.app.FragmentActivity
import androidx.test.filters.LargeTest
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.pressBack
import reactivecircus.blueprint.testing.currentActivity
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickCancelButtonOnDatePicker
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MaterialDatePickerCancelFlowTest {

    @Test
    fun materialDatePickerCancels_pressBackButton() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialDatePicker.Builder.datePicker().build()
            picker.cancels().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)
            recorder.assertNoMoreValues()

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialDatePickerCancels_notEmitWhenClickedNegativeButton() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(System.currentTimeMillis())
                .build()
            picker.cancels().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickCancelButtonOnDatePicker()
            recorder.assertNoMoreValues()

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            pressBack()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialDatePickerCancels_notEmitWhenDismissedProgrammatically() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialDatePicker.Builder.datePicker().build()
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
