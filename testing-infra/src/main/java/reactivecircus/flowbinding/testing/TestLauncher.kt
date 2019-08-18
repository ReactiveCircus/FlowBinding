package reactivecircus.flowbinding.testing

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import reactivecircus.blueprint.testing.RobotActions
import reactivecircus.blueprint.testing.currentActivity

inline fun <reified F : Fragment> launchTest(
    block: TestLauncher.() -> Unit
) {
    launchFragmentInContainer<F>(themeResId = R.style.Theme_MaterialComponents_DayNight)
    Espresso.onIdle()
    val testScope = CoroutineScope(Job() + Dispatchers.Main)
    TestLauncher(testScope).block()
    testScope.cancel()
}

class TestLauncher(val testScope: CoroutineScope) : RobotActions() {

    fun <T : View> getViewById(@IdRes viewId: Int): T {
        return currentActivity()!!.findViewById(viewId)
    }

    fun cancelTestScope() {
        testScope.cancel()
        Espresso.onIdle()
    }
}
