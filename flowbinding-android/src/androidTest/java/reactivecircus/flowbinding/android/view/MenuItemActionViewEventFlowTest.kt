package reactivecircus.flowbinding.android.view

import androidx.test.filters.LargeTest
import kotlinx.coroutines.flow.filterIsInstance
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.android.testutil.TestMenuItem
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MenuItemActionViewEventFlowTest {

    @Test
    fun menuItemActionViewEvents_expand() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<MenuItemActionViewEvent.Expand>(testScope)
            val menuItem = TestMenuItem(rootView.context)
            menuItem.actionViewEvents()
                .filterIsInstance<MenuItemActionViewEvent.Expand>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            menuItem.expandActionView()
            recorder.takeValue().menuItem shouldEqual menuItem
            recorder.assertNoMoreValues()

            cancelTestScope()

            menuItem.collapseActionView()
            menuItem.expandActionView()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun menuItemActionViewEvents_expand_notHandled() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<MenuItemActionViewEvent.Expand>(testScope)
            val menuItem = TestMenuItem(rootView.context)
            menuItem.actionViewEvents { false }
                .filterIsInstance<MenuItemActionViewEvent.Expand>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            menuItem.expandActionView()
            recorder.assertNoMoreValues()

            cancelTestScope()

            menuItem.collapseActionView()
            menuItem.expandActionView()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun menuItemActionViewEvents_collapse() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<MenuItemActionViewEvent.Collapse>(testScope)
            val menuItem = TestMenuItem(rootView.context)
            menuItem.actionViewEvents()
                .filterIsInstance<MenuItemActionViewEvent.Collapse>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            menuItem.expandActionView()
            menuItem.collapseActionView()
            recorder.takeValue().menuItem shouldEqual menuItem
            recorder.assertNoMoreValues()

            cancelTestScope()

            menuItem.expandActionView()
            menuItem.collapseActionView()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun menuItemActionViewEvents_collapse_notHandled() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<MenuItemActionViewEvent.Collapse>(testScope)
            val menuItem = TestMenuItem(rootView.context)
            menuItem.actionViewEvents { false }
                .filterIsInstance<MenuItemActionViewEvent.Collapse>()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            menuItem.expandActionView()
            menuItem.collapseActionView()
            recorder.assertNoMoreValues()

            cancelTestScope()

            menuItem.expandActionView()
            menuItem.collapseActionView()
            recorder.assertNoMoreValues()
        }
    }
}
