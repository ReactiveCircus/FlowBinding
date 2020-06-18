package reactivecircus.flowbinding.android.widget

import android.widget.RatingBar
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class RatingBarRatingChangeEventFlowTest {

    @Test
    fun ratingBarRatingChangeEvents() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<RatingChangeEvent>(testScope)
            val ratingBar = getViewById<RatingBar>(R.id.ratingBar)
            ratingBar.ratingChangeEvents().recordWith(recorder)

            val event1 = recorder.takeValue()
            event1.view shouldEqual ratingBar
            event1.rating shouldEqual 0f
            event1.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            runOnUiThread { ratingBar.rating = 3f }
            val event2 = recorder.takeValue()
            event2.view shouldEqual ratingBar
            event2.rating shouldEqual 3f
            event2.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            runOnUiThread { ratingBar.rating = 5f }
            val event3 = recorder.takeValue()
            event3.view shouldEqual ratingBar
            event3.rating shouldEqual 5f
            event3.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { ratingBar.rating = 0f }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun ratingBarRatingChangeEvents_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<RatingChangeEvent>(testScope)
            val ratingBar = getViewById<RatingBar>(R.id.ratingBar)
            ratingBar.ratingChangeEvents()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { ratingBar.rating = 3f }
            val event = recorder.takeValue()
            event.view shouldEqual ratingBar
            event.rating shouldEqual 3f
            event.fromUser shouldEqual false
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { ratingBar.rating = 5f }
            recorder.assertNoMoreValues()
        }
    }
}
