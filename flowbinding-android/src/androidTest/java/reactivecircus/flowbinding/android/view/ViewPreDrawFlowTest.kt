package reactivecircus.flowbinding.android.view

import android.view.View
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewPreDrawFlowTest {

    @Test
    fun viewPreDraws() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val view = View(rootView.context)

            view.preDraws(proceedDrawingPass = { false }).recordWith(recorder)

            recorder.assertNoMoreValues()

            view.viewTreeObserver.dispatchOnPreDraw()
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)
            recorder.assertNoMoreValues()

            cancelTestScope()

            view.viewTreeObserver.dispatchOnPreDraw()
            recorder.assertNoMoreValues()
        }
    }
}
