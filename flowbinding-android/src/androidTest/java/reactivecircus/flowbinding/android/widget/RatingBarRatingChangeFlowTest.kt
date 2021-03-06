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
class RatingBarRatingChangeFlowTest {

    @Test
    fun ratingBarRatingChanges() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Float>(testScope)
            val ratingBar = getViewById<RatingBar>(R.id.ratingBar)
            ratingBar.ratingChanges().recordWith(recorder)

            assertThat(recorder.takeValue())
                .isEqualTo(0f)
            recorder.assertNoMoreValues()

            runOnUiThread { ratingBar.rating = 3f }
            assertThat(recorder.takeValue())
                .isEqualTo(3f)
            recorder.assertNoMoreValues()

            runOnUiThread { ratingBar.rating = 5f }
            assertThat(recorder.takeValue())
                .isEqualTo(5f)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { ratingBar.rating = 0f }
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun ratingBarRatingChanges_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<Float>(testScope)
            val ratingBar = getViewById<RatingBar>(R.id.ratingBar)
            ratingBar.ratingChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread { ratingBar.rating = 3f }
            assertThat(recorder.takeValue())
                .isEqualTo(3f)
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread { ratingBar.rating = 5f }
            recorder.assertNoMoreValues()
        }
    }
}
