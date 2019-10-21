package reactivecircus.flowbinding.android.widget

import android.view.MenuItem
import android.widget.PopupMenu
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class PopupMenuItemClickFlowTest {

    @Test
    fun popupMenuItemClicks() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val root = getRootView()
            val popupMenu = PopupMenu(root.context, root)
            val menu = popupMenu.menu
            val item1 = menu.add(0, 1, 0, "menu 1")
            val item2 = menu.add(0, 2, 0, "menu 2")

            popupMenu.itemClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            menu.performIdentifierAction(1, 0)
            recorder.takeValue() shouldEqual item1

            menu.performIdentifierAction(2, 0)
            recorder.takeValue() shouldEqual item2

            recorder.assertNoMoreValues()

            cancelTestScope()

            menu.performIdentifierAction(1, 0)
            recorder.assertNoMoreValues()
        }
    }
}
