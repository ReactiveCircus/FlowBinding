package reactivecircus.flowbinding.android.widget

import android.widget.SearchView
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
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
            event1.view shouldEqual searchView
            event1.queryText.toString() shouldEqual ""
            recorder.assertNoMoreValues()

            searchView.setQuery("A", false)
            val event2 = recorder.takeValue() as QueryTextEvent.QueryChanged
            event2.view shouldEqual searchView
            event2.queryText.toString() shouldEqual "A"
            recorder.assertNoMoreValues()

            searchView.setQuery("AB", false)
            val event3 = recorder.takeValue() as QueryTextEvent.QueryChanged
            event3.view shouldEqual searchView
            event3.queryText.toString() shouldEqual "AB"
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
            event1.view shouldEqual searchView
            event1.queryText.toString() shouldEqual ""
            recorder.assertNoMoreValues()

            searchView.setQuery("A", true)
            val event2 = recorder.takeValue() as QueryTextEvent.QueryChanged
            event2.view shouldEqual searchView
            event2.queryText.toString() shouldEqual "A"

            val event3 = recorder.takeValue() as QueryTextEvent.QuerySubmitted
            event3.view shouldEqual searchView
            event3.queryText.toString() shouldEqual "A"
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
            event.view shouldEqual searchView
            event.queryText.toString() shouldEqual "AB"
            recorder.assertNoMoreValues()

            cancelTestScope()

            searchView.setQuery("X", false)
            recorder.assertNoMoreValues()
        }
    }
}
