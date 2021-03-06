package reactivecircus.flowbinding.material

import android.view.View
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.chip.ChipGroup
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickView
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ChipGroupCheckedChangedFlowTest {

    @Test
    fun chipGroupChipCheckedChanges_click() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Int>(testScope)
            val chipGroup = getViewById<ChipGroup>(R.id.chipGroup).apply {
                runOnUiThread { check(R.id.chip1) }
            }
            chipGroup.chipCheckedChanges()
                .recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(R.id.chip1)
            recorder.assertNoMoreValues()

            clickView(R.id.chip2)
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.chip2)
            recorder.assertNoMoreValues()

            clickView(R.id.chip3)
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.chip3)
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(R.id.chip1)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun chipGroupChipCheckedChanges_programmatic() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Int>(testScope)
            val chipGroup = getViewById<ChipGroup>(R.id.chipGroup).apply {
                runOnUiThread { check(R.id.chip1) }
            }
            chipGroup.chipCheckedChanges()
                .recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(R.id.chip1)
            recorder.assertNoMoreValues()

            runOnUiThread { chipGroup.check(R.id.chip2) }
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.chip2)
            recorder.assertNoMoreValues()

            runOnUiThread { chipGroup.check(R.id.chip1) }
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.chip1)

            cancelTestScope()

            runOnUiThread { chipGroup.check(R.id.chip3) }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun chipGroupChipCheckedChanges_unchecked() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Int>(testScope)
            val chipGroup = getViewById<ChipGroup>(R.id.chipGroup)
            chipGroup.chipCheckedChanges()
                .recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(View.NO_ID)
            recorder.assertNoMoreValues()

            clickView(R.id.chip1)
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.chip1)
            recorder.assertNoMoreValues()

            runOnUiThread {
                chipGroup.clearCheck()
            }

            assertThat(recorder.takeValue())
                .isEqualTo(View.NO_ID)
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(R.id.chip1)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun chipGroupChipCheckedChanges_skipInitialValue() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<Int>(testScope)
            val chipGroup = getViewById<ChipGroup>(R.id.chipGroup).apply {
                runOnUiThread { check(R.id.chip1) }
            }
            chipGroup.chipCheckedChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { chipGroup.check(R.id.chip2) }
            assertThat(recorder.takeValue())
                .isEqualTo(R.id.chip2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { chipGroup.check(R.id.chip3) }
            recorder.assertNoMoreValues()
        }
    }
}
