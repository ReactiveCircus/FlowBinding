package reactivecircus.flowbinding.testing

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotActions

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
