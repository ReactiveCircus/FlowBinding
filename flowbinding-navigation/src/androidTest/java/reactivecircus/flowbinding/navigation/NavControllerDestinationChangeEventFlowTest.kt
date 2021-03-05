package reactivecircus.flowbinding.navigation

import androidx.navigation.findNavController
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.testing.currentActivity
import reactivecircus.flowbinding.navigation.fixtures.NavigationFragment
import reactivecircus.flowbinding.navigation.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class NavControllerDestinationChangeEventFlowTest {

    @Test
    fun navControllerDestinationChangeEvents() {
        launchTest<NavigationFragment> {
            val recorder = FlowRecorder<DestinationChangeEvent>(testScope)
            val navController = currentActivity()!!.findNavController(R.id.navHostFragment)

            navController.destinationChangeEvents().recordWith(recorder)

            val initialEvent = recorder.takeValue()
            initialEvent.navController shouldEqual navController
            initialEvent.destination.id shouldEqual R.id.fragmentA
            initialEvent.arguments shouldEqual null
            recorder.assertNoMoreValues()

            runOnUiThread {
                navController.navigate(R.id.fragmentB)
            }
            val event1 = recorder.takeValue()
            event1.navController shouldEqual navController
            event1.destination.id shouldEqual R.id.fragmentB
            event1.arguments shouldEqual null
            recorder.assertNoMoreValues()

            runOnUiThread {
                navController.navigate(R.id.fragmentC)
            }
            val event2 = recorder.takeValue()
            event2.navController shouldEqual navController
            event2.destination.id shouldEqual R.id.fragmentC
            event2.arguments shouldEqual null
            recorder.assertNoMoreValues()

            runOnUiThread {
                navController.navigate(R.id.fragmentA)
            }
            val event3 = recorder.takeValue()
            event3.navController shouldEqual navController
            event3.destination.id shouldEqual R.id.fragmentA
            event3.arguments shouldEqual null
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                navController.navigate(R.id.fragmentB)
            }
            recorder.assertNoMoreValues()
        }
    }
}
