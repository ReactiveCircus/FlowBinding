package reactivecircus.flowbinding.testing

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.internal.CheckableImageButton
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Matcher
import reactivecircus.blueprint.testing.RobotActions
import com.google.android.material.R as MaterialR

fun RobotActions.hardSwipeLeft(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(
        GeneralSwipeAction(
            Swipe.FAST, GeneralLocation.CENTER_RIGHT,
            GeneralLocation.CENTER_LEFT, Press.FINGER
        )
    )
}

fun RobotActions.hardSwipeRight(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(
        GeneralSwipeAction(
            Swipe.FAST, GeneralLocation.CENTER_LEFT,
            GeneralLocation.CENTER_RIGHT, Press.FINGER
        )
    )
}

/**
 * Select the navigation item associated with [menuItemResId]
 * from the navigation view associated with [navigationViewResId].
 * TODO remove once blueprint-testing-robot 1.3.0 is released
 */
fun RobotActions.selectNavigationItem(@IdRes navigationViewResId: Int, @IdRes menuItemResId: Int) {
    Espresso.onView(ViewMatchers.withId(navigationViewResId))
        .perform(NavigationViewActions.navigateTo(menuItemResId))
}

/**
 * Click on either the start of end icon on the [TextInputLayout] associated with [viewId].
 * TODO remove once blueprint-testing-robot 1.3.0 is released
 */
fun RobotActions.clickTextInputLayoutIcon(@IdRes viewId: Int, endIcon: Boolean) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextInputLayout::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val iconResId = if (endIcon) {
                    MaterialR.id.text_input_end_icon
                } else {
                    MaterialR.id.text_input_start_icon
                }
                (view as? TextInputLayout)
                    ?.findViewById<CheckableImageButton>(iconResId)
                    ?.performClick()
            }

            override fun getDescription(): String {
                return "${if (endIcon) "end" else "start"} icon clicked."
            }
        })
}

/**
 * Long click on either the start of end icon on the [TextInputLayout] associated with [viewId].
 * TODO remove once blueprint-testing-robot 1.3.0 is released
 */
fun RobotActions.longClickTextInputLayoutIcon(@IdRes viewId: Int, endIcon: Boolean) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextInputLayout::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val iconResId = if (endIcon) {
                    MaterialR.id.text_input_end_icon
                } else {
                    MaterialR.id.text_input_start_icon
                }
                (view as? TextInputLayout)
                    ?.findViewById<CheckableImageButton>(iconResId)
                    ?.performLongClick()
            }

            override fun getDescription(): String {
                return "${if (endIcon) "end" else "start"} icon long clicked."
            }
        })
}
