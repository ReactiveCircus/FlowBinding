package reactivecircus.flowbinding.preference

import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Test
import reactivecircus.blueprint.testing.action.closeKeyboard
import reactivecircus.blueprint.testing.action.pressBack
import reactivecircus.flowbinding.preference.fixtures.SettingsFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.clickView
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class EditTextPreferenceBindEventFlowTest {

    @Test
    fun editTextPreferenceBindEvents() {
        launchTest<SettingsFragment> {
            val preferenceKey = "editTextPreference"
            val recorder = FlowRecorder<EditTextBindEvent>(testScope)
            val editTextPreference = (fragment as PreferenceFragmentCompat)
                .findPreference<EditTextPreference>(preferenceKey)!!

            editTextPreference.editTextBindEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            clickView(editTextPreference.title)
            recorder.takeValue() shouldBeInstanceOf EditTextBindEvent::class
            recorder.assertNoMoreValues()

            cancelTestScope()

            closeKeyboard(fragment.id)
            pressBack()
            clickView(editTextPreference.title)
            recorder.assertNoMoreValues()
        }
    }
}
