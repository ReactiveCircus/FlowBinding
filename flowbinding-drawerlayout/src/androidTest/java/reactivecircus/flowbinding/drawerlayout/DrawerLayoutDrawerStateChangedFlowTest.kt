package reactivecircus.flowbinding.drawerlayout

import android.view.Gravity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.blueprint.testing.action.closeDrawer
import reactivecircus.blueprint.testing.action.openDrawer
import reactivecircus.flowbinding.drawerlayout.fixtures.DrawerLayoutFragment
import reactivecircus.flowbinding.drawerlayout.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class DrawerLayoutDrawerStateChangedFlowTest {

    @Test
    fun drawerLayoutDrawerStateChanges() {
        launchTest<DrawerLayoutFragment> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val drawerLayout = getViewById<DrawerLayout>(R.id.drawerLayout)

            drawerLayout.drawerStateChanges(Gravity.START).recordWith(recorder)

            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            openDrawer(R.id.drawerLayout)
            assertThat(recorder.takeValue())
                .isTrue()
            recorder.assertNoMoreValues()

            closeDrawer(R.id.drawerLayout)
            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            openDrawer(R.id.drawerLayout)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun drawerLayoutDrawerStateChanges_programmatic() {
        launchTest<DrawerLayoutFragment> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val drawerLayout = getViewById<DrawerLayout>(R.id.drawerLayout)

            drawerLayout.drawerStateChanges(Gravity.START).recordWith(recorder)

            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            runOnUiThread { drawerLayout.openDrawer(Gravity.START) }
            getInstrumentation().waitForIdleSync()
            assertThat(recorder.takeValue())
                .isTrue()
            recorder.assertNoMoreValues()

            runOnUiThread { drawerLayout.closeDrawer(Gravity.START) }
            getInstrumentation().waitForIdleSync()
            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { drawerLayout.openDrawer(Gravity.START) }
            getInstrumentation().waitForIdleSync()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun drawerLayoutDrawerStateChanges_skipInitialValue() {
        launchTest<DrawerLayoutFragment> {
            val recorder = FlowRecorder<Boolean>(testScope)
            val drawerLayout = getViewById<DrawerLayout>(R.id.drawerLayout)

            drawerLayout.drawerStateChanges(Gravity.START)
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { drawerLayout.openDrawer(Gravity.START) }
            getInstrumentation().waitForIdleSync()
            assertThat(recorder.takeValue())
                .isTrue()
            recorder.assertNoMoreValues()

            runOnUiThread { drawerLayout.closeDrawer(Gravity.START) }
            getInstrumentation().waitForIdleSync()
            assertThat(recorder.takeValue())
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { drawerLayout.openDrawer(Gravity.START) }
            getInstrumentation().waitForIdleSync()
            recorder.assertNoMoreValues()
        }
    }
}
