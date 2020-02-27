package reactivecircus.flowbinding.android.view

import android.os.Build
import android.view.View
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@SdkSuppress(minSdkVersion = Build.VERSION_CODES.JELLY_BEAN)
@LargeTest
class ViewDrawFlowTest {

    @Test
    fun viewDraws() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val view = View(rootView.context)

            view.draws().recordWith(recorder)

            recorder.assertNoMoreValues()

            view.viewTreeObserver.dispatchOnDraw()
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            view.viewTreeObserver.dispatchOnDraw()
            recorder.assertNoMoreValues()
        }
    }
}
