package reactivecircus.flowbinding.android.widget

import android.widget.RatingBar
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
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
            assertThat(event1.view)
                .isEqualTo(ratingBar)
            assertThat(event1.rating)
                .isEqualTo(0f)
            assertThat(event1.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            runOnUiThread { ratingBar.rating = 3f }
            val event2 = recorder.takeValue()
            assertThat(event2.view)
                .isEqualTo(ratingBar)
            assertThat(event2.rating)
                .isEqualTo(3f)
            assertThat(event2.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            runOnUiThread { ratingBar.rating = 5f }
            val event3 = recorder.takeValue()
            assertThat(event3.view)
                .isEqualTo(ratingBar)
            assertThat(event3.rating)
                .isEqualTo(5f)
            assertThat(event3.fromUser)
                .isFalse()
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
            assertThat(event.view)
                .isEqualTo(ratingBar)
            assertThat(event.rating)
                .isEqualTo(3f)
            assertThat(event.fromUser)
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { ratingBar.rating = 5f }
            recorder.assertNoMoreValues()
        }
    }
}
