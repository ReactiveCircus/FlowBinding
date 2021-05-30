package reactivecircus.flowbinding.android.widget

import android.widget.PopupMenu
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class PopupMenuDismissFlowTest {

    @Test
    fun popupMenuDismisses() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val popupMenu = PopupMenu(rootView.context, rootView)

            popupMenu.dismisses().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                popupMenu.show()
            }
            recorder.assertNoMoreValues()

            runOnUiThread {
                popupMenu.dismiss()
            }
            assertThat(recorder.takeValue())
                .isEqualTo(Unit)

            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                popupMenu.show()
                popupMenu.dismiss()
            }
            recorder.assertNoMoreValues()
        }
    }
}
