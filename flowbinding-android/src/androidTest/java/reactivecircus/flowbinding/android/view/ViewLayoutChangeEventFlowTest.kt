package reactivecircus.flowbinding.android.view

import android.view.View
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewLayoutChangeEventFlowTest {

    @Test
    fun viewLayoutChangeEvents() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<LayoutChangeEvent>(testScope)
            val view = View(rootView.context)

            view.layoutChangeEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                view.layout(view.left - 5, view.top - 5, view.right, view.bottom)
            }
            val event = recorder.takeValue()
            assertThat(event.view)
                .isEqualTo(view)
            assertThat(event.left)
                .isEqualTo(-5)
            assertThat(event.top)
                .isEqualTo(-5)
            assertThat(event.right)
                .isEqualTo(0)
            assertThat(event.bottom)
                .isEqualTo(0)
            assertThat(event.oldLeft)
                .isEqualTo(0)
            assertThat(event.oldTop)
                .isEqualTo(0)
            assertThat(event.oldRight)
                .isEqualTo(0)
            assertThat(event.oldBottom)
                .isEqualTo(0)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                view.layout(view.left, view.top, view.right, view.bottom)
            }
            recorder.assertNoMoreValues()
        }
    }
}
