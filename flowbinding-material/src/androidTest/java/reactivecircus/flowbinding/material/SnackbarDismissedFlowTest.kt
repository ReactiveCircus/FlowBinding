package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_ACTION
import com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_MANUAL
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickSnackbarActionButton
import reactivecircus.blueprint.testing.asViewAction
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SnackbarDismissedFlowTest {

    @Test
    fun snackbarDismissEvents_actionClick() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Int>(testScope)
            val snackbar = Snackbar.make(
                getViewById(android.R.id.content),
                "Yo",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Dismiss") {}
            snackbar.dismissEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            snackbar.show().asViewAction()
            clickSnackbarActionButton()
            assertThat(recorder.takeValue())
                .isEqualTo(DISMISS_EVENT_ACTION)

            cancelTestScope()
            recorder.clearValues()

            snackbar.show().asViewAction()
            clickSnackbarActionButton()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun snackbarDismissEvents_programmatic() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Int>(testScope)
            val snackbar = Snackbar.make(
                getViewById(android.R.id.content),
                "Yo",
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar.dismissEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            snackbar.show().asViewAction()
            snackbar.dismiss().asViewAction()
            assertThat(recorder.takeValue())
                .isEqualTo(DISMISS_EVENT_MANUAL)

            cancelTestScope()
            recorder.clearValues()

            snackbar.show().asViewAction()
            snackbar.dismiss().asViewAction()
            recorder.assertNoMoreValues()
        }
    }
}
