package reactivecircus.flowbinding.appcompat

import android.view.MenuItem
import androidx.appcompat.widget.ActionMenuView
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.appcompat.fixtures.AppCompatFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ActionMenuViewItemClickFlowTest {

    @Test
    fun actionMenuViewItemClicks() {
        launchTest<AppCompatFragment> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val actionMenuView = ActionMenuView(rootView.context)
            val menu = actionMenuView.menu
            val item1 = menu.add(0, 1, 0, "menu 1")
            val item2 = menu.add(0, 2, 0, "menu 2")

            actionMenuView.itemClicks().recordWith(recorder)

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
