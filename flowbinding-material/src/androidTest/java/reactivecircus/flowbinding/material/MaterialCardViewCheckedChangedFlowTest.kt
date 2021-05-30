package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.card.MaterialCardView
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MaterialCardViewCheckedChangedFlowTest {

    @Test
    fun materialCardViewCheckedChanges() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val cardView = getViewById<MaterialCardView>(R.id.materialCardViewTop).apply {
                runOnUiThread { isCheckable = true }
            }
            cardView.checkedChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            runOnUiThread { cardView.isChecked = true }
            assertThat(recorder.takeValue())
                .isTrue()
            recorder.assertNoMoreValues()

            runOnUiThread { cardView.isChecked = false }
            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { cardView.isChecked = true }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialCardViewCheckedChanges_skipInitialValue() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val cardView = getViewById<MaterialCardView>(R.id.materialCardViewTop).apply {
                runOnUiThread {
                    isCheckable = true
                    isChecked = true
                }
            }
            cardView.checkedChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { cardView.isChecked = false }
            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { cardView.isChecked = true }
            recorder.assertNoMoreValues()
        }
    }
}
