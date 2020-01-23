package reactivecircus.flowbinding.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.lifecycle.fixtures.LifecycleFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class LifecycleEventFlowTest {

    @Test
    fun lifecycleEvents() {
        launchTest<LifecycleFragment> { scenario ->
            val recorder = FlowRecorder<Lifecycle.Event>(testScope)
            fragment.lifecycle.events().recordWith(recorder)

            recorder.takeValue() shouldEqual Lifecycle.Event.ON_CREATE
            recorder.takeValue() shouldEqual Lifecycle.Event.ON_RESUME
            recorder.assertNoMoreValues()

            scenario.moveToState(Lifecycle.State.CREATED)
            recorder.takeValue() shouldEqual Lifecycle.Event.ON_PAUSE
            recorder.takeValue() shouldEqual Lifecycle.Event.ON_STOP
            recorder.assertNoMoreValues()

            cancelTestScope()

            scenario.moveToState(Lifecycle.State.DESTROYED)
            recorder.assertNoMoreValues()
        }
    }
}
