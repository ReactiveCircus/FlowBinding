package reactivecircus.flowbinding.android.widget

import android.widget.PopupMenu
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
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
            val root = getRootView()
            val popupMenu = PopupMenu(root.context, root)

            popupMenu.dismisses().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                popupMenu.show()
            }
            recorder.assertNoMoreValues()

            runOnUiThread {
                popupMenu.dismiss()
            }
            recorder.takeValue() shouldEqual Unit

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
