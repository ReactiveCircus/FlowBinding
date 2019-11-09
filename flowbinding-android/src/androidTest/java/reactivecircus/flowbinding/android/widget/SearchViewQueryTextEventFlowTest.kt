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

            recorder.assertNoMoreValues()

            searchView.setQuery("A", false)
            val event1 = recorder.takeValue() as QueryTextEvent.QueryChanged
            event1.view shouldEqual searchView
            event1.queryText.toString() shouldEqual "A"
            recorder.assertNoMoreValues()

            searchView.setQuery("AB", false)
            val event2 = recorder.takeValue() as QueryTextEvent.QueryChanged
            event2.view shouldEqual searchView
            event2.queryText.toString() shouldEqual "AB"
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

            recorder.assertNoMoreValues()

            searchView.setQuery("A", true)
            val event1 = recorder.takeValue() as QueryTextEvent.QueryChanged
            event1.view shouldEqual searchView
            event1.queryText.toString() shouldEqual "A"

            val event2 = recorder.takeValue() as QueryTextEvent.QuerySubmitted
            event2.view shouldEqual searchView
            event2.queryText.toString() shouldEqual "A"
            recorder.assertNoMoreValues()

            cancelTestScope()

            searchView.setQuery("X", true)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun searchViewQueryTextEvents_emitImmediately() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<QueryTextEvent>(testScope)
            val searchView = getViewById<SearchView>(R.id.searchView).apply {
                setQuery("ABC", false)
            }
            searchView.queryTextEvents(emitImmediately = true).recordWith(recorder)

            val event1 = recorder.takeValue() as QueryTextEvent.QueryChanged
            event1.view shouldEqual searchView
            event1.queryText.toString() shouldEqual "ABC"
            recorder.assertNoMoreValues()

            searchView.setQuery("AB", false)
            val event2 = recorder.takeValue() as QueryTextEvent.QueryChanged
            event2.view shouldEqual searchView
            event2.queryText.toString() shouldEqual "AB"
            recorder.assertNoMoreValues()

            cancelTestScope()

            searchView.setQuery("X", false)
            recorder.assertNoMoreValues()
        }
    }
}
