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
class ViewLayoutChangeEventFlowTest {

    @Test
    fun viewLayoutChangeEvents() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<LayoutChangeEvent>(testScope)
            val view = View(rootView.context)

            view.layoutChangeEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                view.layout(view.left - 5, view.top - 5, view.right, view.bottom)
            }
            val event = recorder.takeValue()
            event.view shouldEqual view
            event.left shouldEqual -5
            event.top shouldEqual -5
            event.right shouldEqual 0
            event.bottom shouldEqual 0
            event.oldLeft shouldEqual 0
            event.oldTop shouldEqual 0
            event.oldRight shouldEqual 0
            event.oldBottom shouldEqual 0
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                view.layout(view.left, view.top, view.right, view.bottom)
            }
            recorder.assertNoMoreValues()
        }
    }
}
