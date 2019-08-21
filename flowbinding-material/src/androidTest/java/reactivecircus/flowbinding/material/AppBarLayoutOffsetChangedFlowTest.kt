package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import com.google.android.material.appbar.AppBarLayout
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.swipeDownOnView
import reactivecircus.blueprint.testing.action.swipeUpOnView
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AppBarLayoutOffsetChangedFlowTest {

    @Test
    fun appBarLayoutOffsetChanges() {
        launchTest<MaterialFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            getViewById<AppBarLayout>(R.id.appBarLayout).offsetChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeUpOnView(R.id.materialTextView)
            val event = recorder.takeValue()
            assertThat(event).isLessThan(0)

            cancelTestScope()
            recorder.clearValues()

            swipeDownOnView(R.id.materialTextView)
            recorder.assertNoMoreValues()
        }
    }
}
