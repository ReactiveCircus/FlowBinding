package reactivecircus.flowbinding.appcompat

import androidx.appcompat.widget.SearchView
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.appcompat.fixtures.AppCompatFragment
import reactivecircus.flowbinding.appcompat.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class SearchViewQueryTextChangeFlowTest {

    @Test
    fun searchViewQueryTextChanges() {
        launchTest<AppCompatFragment> {
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
        launchTest<AppCompatFragment> {
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
