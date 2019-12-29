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

@LargeTest
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.M)
class ViewScrollChangeEventFlowTest {

    @Test
    fun viewScrollChangeEvents() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<ScrollChangeEvent>(testScope)
            val view = View(rootView.context)

            view.scrollChangeEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            view.scrollTo(10, 10)
            val event = recorder.takeValue()
            event.view shouldEqual view
            event.scrollX shouldEqual 10
            event.scrollY shouldEqual 10
            event.oldScrollX shouldEqual 0
            event.oldScrollY shouldEqual 0
            recorder.assertNoMoreValues()

            cancelTestScope()

            view.scrollTo(0, 0)
            recorder.assertNoMoreValues()
        }
    }
}
