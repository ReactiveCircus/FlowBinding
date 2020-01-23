package reactivecircus.flowbinding.activity

import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.action.pressBack
import reactivecircus.flowbinding.activity.fixtures.ActivityFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class OnBackPressedDispatcherBackPressedFlowTest {

    @Test
    fun onBackPressedDispatcherBackPresses() {
        launchTest<ActivityFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            fragment.requireActivity().onBackPressedDispatcher.backPresses(owner = fragment).recordWith(recorder)

            pressBack()
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            pressBack()
            recorder.assertNoMoreValues()
        }
    }
}
