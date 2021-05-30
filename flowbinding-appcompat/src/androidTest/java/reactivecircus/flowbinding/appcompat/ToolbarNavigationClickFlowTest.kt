package reactivecircus.flowbinding.appcompat

import androidx.appcompat.widget.Toolbar
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickNavigateUpButton
import reactivecircus.flowbinding.appcompat.fixtures.AppCompatFragment
import reactivecircus.flowbinding.appcompat.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ToolbarNavigationClickFlowTest {

    @Test
    fun toolbarNavigationClicks() {
        launchTest<AppCompatFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val toolbar = getViewById<Toolbar>(R.id.toolbar)

            toolbar.navigationClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            clickNavigateUpButton()
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)

            recorder.assertNoMoreValues()

            cancelTestScope()

            clickNavigateUpButton()
            recorder.assertNoMoreValues()
        }
    }
}
