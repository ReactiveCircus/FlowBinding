package reactivecircus.flowbinding.appcompat

import androidx.appcompat.widget.PopupMenu
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.appcompat.fixtures.AppCompatFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class PopupMenuDismissFlowTest {

    @Test
    fun popupMenuDismisses() {
        launchTest<AppCompatFragment> {
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
