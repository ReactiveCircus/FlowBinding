package reactivecircus.flowbinding.material

import android.view.MenuItem
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.selectBottomNavigationItem
import reactivecircus.flowbinding.material.fixtures.MaterialFragment2
import reactivecircus.flowbinding.material.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class BottomNavigationViewItemReselectedFlowTest {

    @Test
    fun bottomNavigationViewItemReselections_manual() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val bottomNavigationView = getViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.itemReselections().recordWith(recorder)

            recorder.assertNoMoreValues()

            selectBottomNavigationItem(
                R.id.bottomNavigationView,
                bottomNavigationView.menu.getItem(1).title.toString()
            )
            recorder.assertNoMoreValues()

            selectBottomNavigationItem(
                R.id.bottomNavigationView,
                bottomNavigationView.menu.getItem(1).title.toString()
            )
            assertThat(recorder.takeValue().itemId)
                .isEqualTo(R.id.dest2)
            recorder.assertNoMoreValues()

            cancelTestScope()

            selectBottomNavigationItem(
                R.id.bottomNavigationView,
                bottomNavigationView.menu.getItem(1).title.toString()
            )
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun bottomNavigationViewItemReselections_programmatic() {
        launchTest<MaterialFragment2> {
            val recorder = FlowRecorder<MenuItem>(testScope)
            val bottomNavigationView = getViewById<BottomNavigationView>(R.id.bottomNavigationView)
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
