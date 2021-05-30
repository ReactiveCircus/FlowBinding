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
class SearchViewQueryTextEventFlowTest {

    @Test
    fun searchViewQueryTextEvents_queryChanged() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<QueryTextEvent>(testScope)
            val searchView = getViewById<SearchView>(R.id.searchView)
            searchView.queryTextEvents().recordWith(recorder)

            val event1 = recorder.takeValue() as QueryTextEvent.QueryChanged
            assertThat(event1.view)
                .isEqualTo(searchView)
            assertThat(event1.queryText.toString())
                .isEmpty()
            recorder.assertNoMoreValues()

            searchView.setQuery("A", false)
            val event2 = recorder.takeValue() as QueryTextEvent.QueryChanged
            assertThat(event2.view)
                .isEqualTo(searchView)
            assertThat(event2.queryText.toString())
                .isEqualTo("A")
            recorder.assertNoMoreValues()

            searchView.setQuery("AB", false)
            val event3 = recorder.takeValue() as QueryTextEvent.QueryChanged
            assertThat(event3.view)
                .isEqualTo(searchView)
            assertThat(event3.queryText.toString())
                .isEqualTo("AB")
            recorder.assertNoMoreValues()

            cancelTestScope()

            searchView.setQuery("X", false)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun searchViewQueryTextEvents_querySubmitted() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<QueryTextEvent>(testScope)
            val searchView = getViewById<SearchView>(R.id.searchView)
            searchView.queryTextEvents().recordWith(recorder)

            val event1 = recorder.takeValue() as QueryTextEvent.QueryChanged
            assertThat(event1.view)
                .isEqualTo(searchView)
            assertThat(event1.queryText.toString())
                .isEmpty()
            recorder.assertNoMoreValues()

            searchView.setQuery("A", true)
            val event2 = recorder.takeValue() as QueryTextEvent.QueryChanged
            assertThat(event2.view)
                .isEqualTo(searchView)
            assertThat(event2.queryText.toString())
                .isEqualTo("A")

            val event3 = recorder.takeValue() as QueryTextEvent.QuerySubmitted
            assertThat(event3.view)
                .isEqualTo(searchView)
            assertThat(event3.queryText.toString())
                .isEqualTo("A")
            recorder.assertNoMoreValues()

            cancelTestScope()

            searchView.setQuery("X", true)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun searchViewQueryTextEvents_skipInitialValue() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<QueryTextEvent>(testScope)
            val searchView = getViewById<SearchView>(R.id.searchView).apply {
                setQuery("ABC", false)
            }
            searchView.queryTextEvents()
                .skipInitialValue()
                .recordWith(recorder)

            recorder.assertNoMoreValues()

            searchView.setQuery("AB", false)
            val event = recorder.takeValue() as QueryTextEvent.QueryChanged
            assertThat(event.view)
                .isEqualTo(searchView)
            assertThat(event.queryText.toString())
                .isEqualTo("AB")
            recorder.assertNoMoreValues()

            cancelTestScope()

            searchView.setQuery("X", false)
            recorder.assertNoMoreValues()
        }
    }
}
