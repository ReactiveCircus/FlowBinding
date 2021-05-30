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

@SdkSuppress(minSdkVersion = Build.VERSION_CODES.JELLY_BEAN)
@LargeTest
class ViewGlobalLayoutFlowTest {

    @Test
    fun viewGlobalLayouts() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val view = View(rootView.context)

            view.globalLayouts().recordWith(recorder)

            recorder.assertNoMoreValues()

            view.viewTreeObserver.dispatchOnGlobalLayout()
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)
            recorder.assertNoMoreValues()

            cancelTestScope()

            view.viewTreeObserver.dispatchOnGlobalLayout()
            recorder.assertNoMoreValues()
        }
    }
}
