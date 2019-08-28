package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.chip.Chip
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ChipCloseIconClickedFlowTest {

    @Test
    fun chipCloseIconClicks() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Unit>(testScope)
            val chip1 = getViewById<Chip>(R.id.chip1)
            chip1.closeIconClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { chip1.performCloseIconClick() }
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            val chip2 = getViewById<Chip>(R.id.chip2)
            chip2.closeIconClicks().recordWith(recorder)
            runOnUiThread { chip2.performCloseIconClick() }
            recorder.assertNoMoreValues()
        }
    }
}
