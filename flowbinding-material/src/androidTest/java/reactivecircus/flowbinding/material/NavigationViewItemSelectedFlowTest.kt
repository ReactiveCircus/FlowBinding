package reactivecircus.flowbinding.material

import android.view.MenuItem
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.navigation.NavigationView
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.openDrawer
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith
import reactivecircus.flowbinding.testing.selectNavigationItem

@LargeTest
class NavigationViewItemSelectedFlowTest {

    @Test
    fun navigationViewItemSelections() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val navigationView = getViewById<NavigationView>(R.id.navigationView)
            navigationView.itemSelections().recordWith(recorder)

            recorder.assertNoMoreValues()

            openDrawer(R.id.drawerLayout)
            selectNavigationItem(R.id.navigationView, R.id.item2)
            recorder.takeValue().itemId shouldEqual R.id.item2
            recorder.assertNoMoreValues()

            cancelTestScope()

            selectNavigationItem(R.id.navigationView, R.id.item3)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun navigationViewItemSelections_emitImmediately() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val navigationView = getViewById<NavigationView>(R.id.navigationView).apply {
                runOnUiThread { setCheckedItem(R.id.item1) }
            }
            navigationView.itemSelections(emitImmediately = true).recordWith(recorder)

            recorder.takeValue().itemId shouldEqual R.id.item1
            recorder.assertNoMoreValues()

            openDrawer(R.id.drawerLayout)
            selectNavigationItem(R.id.navigationView, R.id.item2)
            recorder.takeValue().itemId shouldEqual R.id.item2
            recorder.assertNoMoreValues()

            cancelTestScope()

            selectNavigationItem(R.id.navigationView, R.id.item1)
            recorder.assertNoMoreValues()
        }
    }
}
