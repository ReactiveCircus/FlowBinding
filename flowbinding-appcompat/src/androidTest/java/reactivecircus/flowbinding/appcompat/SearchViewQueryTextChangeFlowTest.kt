package reactivecircus.flowbinding.appcompat

import androidx.appcompat.widget.SearchView
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.appcompat.fixtures.AppCompatFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SearchViewQueryTextChangeFlowTest {

    @Test
    fun searchViewQueryTextChanges() {
        launchTest<AppCompatFragment> {
            val recorder = FlowRecorder<CharSequence>(testScope)
            val searchView = SearchView(getRootView().context)
            searchView.queryTextChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            searchView.setQuery("A", false)
            recorder.takeValue().toString() shouldEqual "A"
            recorder.assertNoMoreValues()

            searchView.setQuery("AB", false)
            recorder.takeValue().toString() shouldEqual "AB"
            recorder.assertNoMoreValues()

            cancelTestScope()

            searchView.setQuery("X", false)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun searchViewQueryTextChanges_emitImmediately() {
        launchTest<AppCompatFragment> {
            val recorder = FlowRecorder<CharSequence>(testScope)
            val searchView = SearchView(getRootView().context).apply {
                setQuery("ABC", false)
            }
            searchView.queryTextChanges(emitImmediately = true).recordWith(recorder)

            recorder.takeValue().toString() shouldEqual "ABC"
            recorder.assertNoMoreValues()

            searchView.setQuery("AB", false)
            recorder.takeValue().toString() shouldEqual "AB"
            recorder.assertNoMoreValues()

            cancelTestScope()

            searchView.setQuery("X", false)
            recorder.assertNoMoreValues()
        }
    }
}
