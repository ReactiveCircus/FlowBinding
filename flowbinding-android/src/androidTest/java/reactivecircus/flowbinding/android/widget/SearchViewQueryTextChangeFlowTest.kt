package reactivecircus.flowbinding.android.widget

import android.content.Context
import android.widget.SearchView
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.widget.AndroidWidgetFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SearchViewQueryTextChangeFlowTest {

    private val appContext = ApplicationProvider.getApplicationContext<Context>().applicationContext

    @Test
    fun searchViewQueryTextChanges() {
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<CharSequence>(testScope)
            val searchView = SearchView(appContext)
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
        launchTest<AndroidWidgetFragment> {
            val recorder = FlowRecorder<CharSequence>(testScope)
            val searchView = SearchView(appContext).apply {
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
