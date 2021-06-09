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
class NavigationBarViewItemSelectedFlowTest {

    @Test
    fun navigationBarViewItemSelections_manual() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val bottomNavigationView = getViewById<NavigationBarView>(R.id.bottomNavigationView)
            bottomNavigationView.itemSelections().recordWith(recorder)

            assertThat(recorder.takeValue().itemId)
                .isEqualTo(R.id.dest1)
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
                bottomNavigationView.menu.getItem(2).title.toString()
            )
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun navigationBarViewItemSelections_programmatic() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val bottomNavigationView = getViewById<NavigationBarView>(R.id.bottomNavigationView)
            bottomNavigationView.itemSelections().recordWith(recorder)

            assertThat(recorder.takeValue().itemId)
                .isEqualTo(R.id.dest1)
            recorder.assertNoMoreValues()

            runOnUiThread { bottomNavigationView.selectedItemId = R.id.dest2 }
            assertThat(recorder.takeValue().itemId)
                .isEqualTo(R.id.dest2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { bottomNavigationView.selectedItemId = R.id.dest3 }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun navigationBarViewItemSelections_noMenu() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val bottomNavigationView = getViewById<NavigationBarView>(R.id.bottomNavigationView).apply {
                runOnUiThread { menu.clear() }
            }
            bottomNavigationView.itemSelections().recordWith(recorder)

            recorder.assertNoMoreValues()

            cancelTestScope()
        }
    }
}
