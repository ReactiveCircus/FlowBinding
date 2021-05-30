package reactivecircus.flowbinding.android.view

import android.widget.EditText
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickView
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewFocusChangedFlowTest {

    @Test
    fun viewFocusChanges() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val editText = getViewById<EditText>(R.id.editText1)
            editText.focusChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            clickView(R.id.editText1)
            assertThat(recorder.takeValue())
                .isTrue()
            recorder.assertNoMoreValues()

            clickView(R.id.editText2)
            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(R.id.editText1)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun viewFocusChanges_programmatic() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val editText1 = getViewById<EditText>(R.id.editText1)
            val editText2 = getViewById<EditText>(R.id.editText2)
            editText1.focusChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            runOnUiThread { editText1.requestFocus() }
            assertThat(recorder.takeValue())
                .isTrue()
            recorder.assertNoMoreValues()

            runOnUiThread { editText2.requestFocus() }
            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { editText1.requestFocus() }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun viewFocusChanges_skipInitialValue() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val editText = getViewById<EditText>(R.id.editText1).apply {
                runOnUiThread { requestFocus() }
            }
            editText.focusChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            clickView(R.id.editText2)
            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(R.id.editText1)
            recorder.assertNoMoreValues()
        }
    }
}
