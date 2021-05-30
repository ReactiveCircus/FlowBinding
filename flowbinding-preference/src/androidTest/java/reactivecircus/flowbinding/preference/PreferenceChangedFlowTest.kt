package reactivecircus.flowbinding.preference

import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.preference.fixtures.SettingsFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickView
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class PreferenceChangedFlowTest {

    @Test
    fun preferenceChanges() {
        launchTest<SettingsFragment> {
            val preferenceKey = "switchPreference"
            val recorder = FlowRecorder<Any>(testScope)
            val preference = (fragment as PreferenceFragmentCompat)
                .findPreference<SwitchPreferenceCompat>(preferenceKey)!!

            preference.preferenceChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            clickView(preference.title)
            assertThat(recorder.takeValue() as Boolean)
                .isTrue()
            recorder.assertNoMoreValues()

            clickView(preference.title)
            assertThat(recorder.takeValue() as Boolean)
                .isFalse()
            recorder.assertNoMoreValues()

            cancelTestScope()

            clickView(preference.title)
            recorder.assertNoMoreValues()
        }
    }
}
