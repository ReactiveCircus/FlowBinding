package reactivecircus.flowbinding.material

import androidx.fragment.app.FragmentActivity
import androidx.test.filters.LargeTest
import com.google.android.material.datepicker.MaterialDatePicker
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.currentActivity
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickOkButtonOnDatePicker
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith
import java.util.Calendar
import java.util.TimeZone

@LargeTest
class MaterialDatePickerPositiveButtonClickFlowTest {

    @Test
    fun materialDatePickerPositiveButtonClicks() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Long>(testScope)
            val timestamp = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                set(Calendar.YEAR, 2019)
                set(Calendar.MONTH, 9)
                set(Calendar.MINUTE, 28)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(timestamp)
                .build()
            picker.positiveButtonClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickOkButtonOnDatePicker()
            recorder.takeValue() shouldEqual timestamp

            cancelTestScope()
            recorder.clearValues()

            picker.show((currentActivity() as FragmentActivity).supportFragmentManager, toString())
            clickOkButtonOnDatePicker()
            recorder.assertNoMoreValues()
        }
    }
}
