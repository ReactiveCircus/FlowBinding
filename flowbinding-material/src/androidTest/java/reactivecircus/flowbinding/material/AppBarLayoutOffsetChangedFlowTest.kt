package reactivecircus.flowbinding.material

import androidx.test.filters.LargeTest
import com.google.android.material.appbar.AppBarLayout
import org.amshove.kluent.shouldBeLessThan
import org.junit.Test
import reactivecircus.blueprint.testing.action.swipeDownOnView
import reactivecircus.blueprint.testing.action.swipeUpOnView
import reactivecircus.flowbinding.material.fixtures.MaterialFragment1
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class AppBarLayoutOffsetChangedFlowTest {

    @Test
    fun appBarLayoutOffsetChanges() {
        launchTest<MaterialFragment1> {
            val recorder = FlowRecorder<Int>(testScope)
            getViewById<AppBarLayout>(R.id.appBarLayout).offsetChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeUpOnView(R.id.materialTextView)
            recorder.takeValue() shouldBeLessThan 0

            cancelTestScope()
            recorder.clearValues()

            swipeDownOnView(R.id.materialTextView)
            recorder.assertNoMoreValues()
        }
    }
}
