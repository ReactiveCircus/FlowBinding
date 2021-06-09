package reactivecircus.flowbinding.material

import android.view.MenuItem
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.navigation.NavigationBarView
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.selectNavigationBarItem
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class NavigationBarViewItemReselectedFlowTest {

    @Test
    fun navigationBarViewItemReselections_manual() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val bottomNavigationView = getViewById<NavigationBarView>(R.id.bottomNavigationView)
            bottomNavigationView.itemReselections().recordWith(recorder)

            recorder.assertNoMoreValues()

            selectNavigationBarItem(
                R.id.bottomNavigationView,
                bottomNavigationView.menu.getItem(1).title.toString()
            )
            recorder.assertNoMoreValues()

            selectNavigationBarItem(
                R.id.bottomNavigationView,
                bottomNavigationView.menu.getItem(1).title.toString()
            )
            assertThat(recorder.takeValue().itemId)
                .isEqualTo(R.id.dest2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            selectNavigationBarItem(
                R.id.bottomNavigationView,
                bottomNavigationView.menu.getItem(1).title.toString()
            )
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun navigationBarViewItemReselections_programmatic() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val bottomNavigationView = getViewById<NavigationBarView>(R.id.bottomNavigationView)
            bottomNavigationView.itemReselections().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { bottomNavigationView.selectedItemId = R.id.dest2 }
            recorder.assertNoMoreValues()

            runOnUiThread { bottomNavigationView.selectedItemId = R.id.dest2 }
            assertThat(recorder.takeValue().itemId)
                .isEqualTo(R.id.dest2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { bottomNavigationView.selectedItemId = R.id.dest2 }
            recorder.assertNoMoreValues()
        }
    }
}
