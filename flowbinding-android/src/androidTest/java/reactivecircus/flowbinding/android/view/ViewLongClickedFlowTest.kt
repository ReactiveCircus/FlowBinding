package reactivecircus.flowbinding.android.view

import android.view.View
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.longClickView
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewLongClickedFlowTest {

    @Test
    fun viewLongClicks() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val view = getViewById<View>(R.id.button)
            view.longClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            longClickView(R.id.button)
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            longClickView(R.id.button)
            recorder.assertNoMoreValues()
        }
    }
}
