package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.card.MaterialCardView
import org.amshove.kluent.shouldEqual
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

            recorder.assertNoMoreValues()

            runOnUiThread { cardView.isChecked = true }
            recorder.takeValue() shouldEqual true
            recorder.assertNoMoreValues()

            runOnUiThread { cardView.isChecked = false }
            recorder.takeValue() shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { cardView.isChecked = true }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun materialCardViewCheckedChanges_emitImmediately() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val cardView = getViewById<MaterialCardView>(R.id.materialCardViewTop).apply {
                runOnUiThread {
                    isCheckable = true
                    isChecked = true
                }
            }
            cardView.checkedChanges(emitImmediately = true).recordWith(recorder)

            recorder.takeValue() shouldEqual true
            recorder.assertNoMoreValues()

            runOnUiThread { cardView.isChecked = false }
            recorder.takeValue() shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { cardView.isChecked = true }
            recorder.assertNoMoreValues()
        }
    }
}
