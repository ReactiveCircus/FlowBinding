package reactivecircus.flowbinding.swiperefreshlayout

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.swipeDownOnView
import reactivecircus.flowbinding.swiperefreshlayout.fixtures.SwipeRefreshLayoutFragment
import reactivecircus.flowbinding.swiperefreshlayout.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SwipeRefreshLayoutRefreshFlowTest {

    @Test
    fun swipeRefreshLayoutRefreshes() {
        launchTest<SwipeRefreshLayoutFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val swipeRefreshLayout = getViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
            swipeRefreshLayout.refreshes().recordWith(recorder)

            recorder.assertNoMoreValues()

            swipeDownOnView(R.id.swipeRefreshLayout)
            recorder.takeValue() shouldEqual Unit

            cancelTestScope()
            recorder.clearValues()

            swipeDownOnView(R.id.swipeRefreshLayout)
            recorder.assertNoMoreValues()
        }
    }
}
