package reactivecircus.flowbinding.android.view

import android.os.Build
import android.view.View
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@SdkSuppress(minSdkVersion = Build.VERSION_CODES.M)
@LargeTest
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
            assertThat(event.view)
                .isEqualTo(view)
            assertThat(event.scrollX)
                .isEqualTo(10)
            assertThat(event.scrollY)
                .isEqualTo(10)
            assertThat(event.oldScrollX)
                .isEqualTo(0)
            assertThat(event.oldScrollY)
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            cancelTestScope()

            view.scrollTo(0, 0)
            recorder.assertNoMoreValues()
        }
    }
}
