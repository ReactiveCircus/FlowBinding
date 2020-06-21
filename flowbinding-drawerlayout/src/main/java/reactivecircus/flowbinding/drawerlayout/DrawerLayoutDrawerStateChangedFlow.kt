package reactivecircus.flowbinding.drawerlayout

import android.view.View
import androidx.annotation.CheckResult
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [InitialValueFlow] of drawer state change events on the [DrawerLayout] instance
 * where the value emitted is a boolean representing whether the drawer is opened or closed.
 *
 * @param gravity the gravity of the [DrawerLayout] to observer.
 *
 * Note: Created flow keeps a strong reference to the [DrawerLayout] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * drawerLayout.drawerStateChanges()
 *     .onEach { sate ->
 *          // handle drawer opened / closed
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun DrawerLayout.drawerStateChanges(gravity: Int): InitialValueFlow<Boolean> =
    callbackFlow<Boolean> {
        checkMainThread()
        val listener = object : DrawerLayout.DrawerListener {
            override fun onDrawerOpened(drawerView: View) {
                val drawerGravity = (drawerView.layoutParams as DrawerLayout.LayoutParams).gravity
                if (drawerGravity == gravity) {
                    safeOffer(true)
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                val drawerGravity = (drawerView.layoutParams as DrawerLayout.LayoutParams).gravity
                if (drawerGravity == gravity) {
                    safeOffer(false)
                }
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit

            override fun onDrawerStateChanged(newState: Int) = Unit
        }
        addDrawerListener(listener)
        awaitClose { removeDrawerListener(listener) }
    }
        .conflate()
        .asInitialValueFlow { isDrawerOpen(gravity) }
