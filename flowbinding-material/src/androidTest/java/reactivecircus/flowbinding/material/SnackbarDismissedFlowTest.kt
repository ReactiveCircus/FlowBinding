package reactivecircus.flowbinding.material

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.test.filters.LargeTest
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_ACTION
import com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_MANUAL
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickSnackbarActionButton
import reactivecircus.blueprint.testing.asViewAction
import reactivecircus.flowbinding.material.fixtures.MaterialFragment
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SnackbarDismissedFlowTest {

    @Test
    fun snackbarDismissEvents_actionClick() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val snackbar = Snackbar.make(
                getViewById<CoordinatorLayout>(R.id.rootView),
                "Yo",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Dismiss") {}
            snackbar.dismissEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            snackbar.show().asViewAction()
            clickSnackbarActionButton()
            recorder.takeValue() shouldEqual DISMISS_EVENT_ACTION

            cancelTestScope()
            recorder.clearValues()

            snackbar.show().asViewAction()
            clickSnackbarActionButton()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun snackbarDismissEvents_programmatic() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val snackbar = Snackbar.make(
                getViewById<CoordinatorLayout>(R.id.rootView),
                "Yo",
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar.dismissEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            snackbar.show().asViewAction()
            snackbar.dismiss().asViewAction()
            recorder.takeValue() shouldEqual DISMISS_EVENT_MANUAL

            cancelTestScope()
            recorder.clearValues()

            snackbar.show().asViewAction()
            snackbar.dismiss().asViewAction()
            recorder.assertNoMoreValues()
        }
    }
}
