package reactivecircus.flowbinding.android.widget

import android.widget.SearchView
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.android.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SearchViewQueryTextChangeFlowTest {

    @Test
    fun searchViewQueryTextChanges() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<CharSequence>(testScope)
            val searchView = getViewById<SearchView>(R.id.searchView)
            searchView.queryTextChanges().recordWith(recorder)

            assertThat(recorder.takeValue().toString())
                .isEmpty()
            recorder.assertNoMoreValues()

            searchView.setQuery("A", false)
            assertThat(recorder.takeValue().toString())
                .isEqualTo("A")
            recorder.assertNoMoreValues()

            searchView.setQuery("AB", false)
            assertThat(recorder.takeValue().toString())
                .isEqualTo("AB")
            recorder.assertNoMoreValues()

            cancelTestScope()

            searchView.setQuery("X", false)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun searchViewQueryTextChanges_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<CharSequence>(testScope)
            val searchView = getViewById<SearchView>(R.id.searchView).apply {
                setQuery("ABC", false)
            }
            searchView.queryTextChanges()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            searchView.setQuery("AB", false)
            assertThat(recorder.takeValue().toString())
                .isEqualTo("AB")
            recorder.assertNoMoreValues()

            cancelTestScope()

            searchView.setQuery("X", false)
            recorder.assertNoMoreValues()
        }
    }
}
