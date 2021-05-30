package reactivecircus.flowbinding.navigation

import androidx.navigation.findNavController
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
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
            assertThat(initialEvent.navController)
                .isEqualTo(navController)
            assertThat(initialEvent.destination.id)
                .isEqualTo(R.id.fragmentA)
            assertThat(initialEvent.arguments)
                .isNull()
            recorder.assertNoMoreValues()

            runOnUiThread {
                navController.navigate(R.id.fragmentB)
            }
            val event1 = recorder.takeValue()
            assertThat(event1.navController)
                .isEqualTo(navController)
            assertThat(event1.destination.id)
                .isEqualTo(R.id.fragmentB)
            assertThat(event1.arguments)
                .isNull()
            recorder.assertNoMoreValues()

            runOnUiThread {
                navController.navigate(R.id.fragmentC)
            }
            val event2 = recorder.takeValue()
            assertThat(event2.navController)
                .isEqualTo(navController)
            assertThat(event2.destination.id)
                .isEqualTo(R.id.fragmentC)
            assertThat(event2.arguments)
                .isNull()
            recorder.assertNoMoreValues()

            runOnUiThread {
                navController.navigate(R.id.fragmentA)
            }
            val event3 = recorder.takeValue()
            assertThat(event3.navController)
                .isEqualTo(navController)
            assertThat(event3.destination.id)
                .isEqualTo(R.id.fragmentA)
            assertThat(event3.arguments)
                .isNull()
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                navController.navigate(R.id.fragmentB)
            }
            recorder.assertNoMoreValues()
        }
    }
}
