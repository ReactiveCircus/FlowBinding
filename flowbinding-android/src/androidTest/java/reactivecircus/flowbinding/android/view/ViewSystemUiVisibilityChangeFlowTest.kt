package reactivecircus.flowbinding.android.view

import android.view.View
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewSystemUiVisibilityChangeFlowTest {

    @Test
    fun viewLayoutChanges() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Int>(testScope)
            val rootView = getRootView().apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }

            rootView.systemUiVisibilityChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                rootView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
            }
            recorder.takeValue() shouldEqual View.SYSTEM_UI_FLAG_LOW_PROFILE
            recorder.assertNoMoreValues()

            runOnUiThread {
                rootView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
            recorder.takeValue() shouldEqual View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            recorder.assertNoMoreValues()

            runOnUiThread {
                rootView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            }
            recorder.takeValue() shouldEqual View.SYSTEM_UI_FLAG_FULLSCREEN
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                rootView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
            recorder.assertNoMoreValues()
        }
    }
}
