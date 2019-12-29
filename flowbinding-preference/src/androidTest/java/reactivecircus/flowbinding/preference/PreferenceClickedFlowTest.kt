package reactivecircus.flowbinding.preference

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.preference.fixtures.SettingsFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickView
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class PreferenceClickedFlowTest {

    @Test
    fun preferenceClicks() {
        launchTest<SettingsFragment> {
            val preferenceKey = "regularPreference"
            val recorder = FlowRecorder<Unit>(testScope)
            val preference = (fragment as PreferenceFragmentCompat)
                .findPreference<Preference>(preferenceKey)!!

            preference.preferenceClicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            clickView(preference.title)
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(preference.title)
            recorder.assertNoMoreValues()
        }
    }
}
