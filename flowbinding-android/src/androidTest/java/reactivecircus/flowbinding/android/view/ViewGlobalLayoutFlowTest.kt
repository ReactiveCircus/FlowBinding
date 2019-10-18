package reactivecircus.flowbinding.android.view

import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewGlobalLayoutFlowTest {

    private val appContext = ApplicationProvider.getApplicationContext<Context>().applicationContext

    @Test
    fun viewGlobalLayouts() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val view = View(appContext)

            view.globalLayouts().recordWith(recorder)

            recorder.assertNoMoreValues()

            view.viewTreeObserver.dispatchOnGlobalLayout()
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            view.viewTreeObserver.dispatchOnGlobalLayout()
            recorder.assertNoMoreValues()
        }
    }
}
