package reactivecircus.flowbinding.material

import androidx.fragment.app.FragmentActivity
import androidx.test.filters.LargeTest
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.currentActivity
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickCancelButtonOnDatePicker
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MaterialDatePickerNegativeButtonClickFlowTest {

    @Test
    fun materialDatePickerNegativeButtonClicks() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Unit>(testScope)
            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(System.currentTimeMillis())
                .build()
            picker.negativeButtonClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickCancelButtonOnDatePicker()
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickCancelButtonOnDatePicker()
            recorder.assertNoMoreValues()
        }
    }
}
