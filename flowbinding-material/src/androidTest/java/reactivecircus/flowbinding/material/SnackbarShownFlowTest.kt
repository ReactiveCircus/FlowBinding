package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import com.google.android.material.snackbar.Snackbar
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.asViewAction
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SnackbarShownFlowTest {

    @Test
    fun snackbarShownEvents() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val snackbar = Snackbar.make(
                getViewById(R.id.rootView),
                "Yo",
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar.shownEvents()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            snackbar.show().asViewAction()
            val event = recorder.takeValue()
            assertThat(event).isEqualTo(Unit)

            cancelTestScope()
            recorder.clearValues()

            snackbar.dismiss().asViewAction()
            snackbar.show().asViewAction()
            recorder.assertNoMoreValues()
        }
    }
}
