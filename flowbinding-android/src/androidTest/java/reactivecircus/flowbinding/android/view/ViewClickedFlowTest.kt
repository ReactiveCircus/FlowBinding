package reactivecircus.flowbinding.android.view

import android.view.View
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.clickView
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewClickedFlowTest {

    @Test
    fun viewClicks() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val view = getViewById<View>(R.id.button)
            view.clicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            clickView(R.id.button)
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(R.id.button)
            recorder.assertNoMoreValues()
        }
    }
}
