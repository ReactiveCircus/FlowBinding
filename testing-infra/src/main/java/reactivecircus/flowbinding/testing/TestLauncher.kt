package reactivecircus.flowbinding.testing

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import reactivecircus.blueprint.testing.RobotActions
import reactivecircus.blueprint.testing.currentActivity
import com.google.android.material.R as MaterialR

inline fun <reified F : Fragment> launchTest(
    block: TestLauncher.() -> Unit
) {
    launchFragmentInContainer<F>(themeResId = MaterialR.style.Theme_MaterialComponents_DayNight)
    Espresso.onIdle()
    val testScope = MainScope()
    TestLauncher(testScope).block()
    testScope.cancel()
}

class TestLauncher(val testScope: CoroutineScope) : RobotActions {

    fun <T : View> getViewById(@IdRes viewId: Int): T {
        return currentActivity()!!.findViewById(viewId)
    }

    fun getRootView(): View {
        return currentActivity()!!.window.decorView.rootView
    }

    fun cancelTestScope() {
        testScope.cancel()
        Espresso.onIdle()
    }
}
