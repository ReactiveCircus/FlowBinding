package reactivecircus.flowbinding.testing

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.CoreMatchers.anything
import reactivecircus.blueprint.testing.RobotActions
import com.google.android.material.R as MaterialR

fun RobotActions.hardSwipeLeft(@IdRes viewId: Int) {
    Espresso.onView(withId(viewId)).perform(
        GeneralSwipeAction(
            Swipe.FAST,
            GeneralLocation.CENTER_RIGHT,
            GeneralLocation.CENTER_LEFT,
            Press.FINGER
        )
    )
}

fun RobotActions.hardSwipeRight(@IdRes viewId: Int) {
    Espresso.onView(withId(viewId)).perform(
        GeneralSwipeAction(
            Swipe.FAST,
            GeneralLocation.CENTER_LEFT,
            GeneralLocation.CENTER_RIGHT,
            Press.FINGER
        )
    )
}

fun RobotActions.clickPopupItemAt(position: Int) {
    Espresso.onData(anything())
        .inRoot(isPlatformPopup())
        .atPosition(position)
        .perform(click())
}

fun RobotActions.longClickAdapterViewItemAt(@IdRes viewId: Int, position: Int) {
    Espresso.onData(anything())
        .inAdapterView(withId(viewId))
        .atPosition(position)
        .perform(longClick())
}

fun RobotActions.clickOkButtonOnDatePicker() {
    Espresso.onView(withId(MaterialR.id.confirm_button))
        .inRoot(isDialog())
        .perform(click())
    getInstrumentation().waitForIdleSync()
}

fun RobotActions.clickCancelButtonOnDatePicker() {
    Espresso.onView(withId(MaterialR.id.cancel_button))
        .inRoot(isDialog())
        .perform(click())
    getInstrumentation().waitForIdleSync()
}

fun RobotActions.clickOkButtonOnTimePicker() {
    Espresso.onView(withId(MaterialR.id.material_timepicker_ok_button))
        .inRoot(isDialog())
        .perform(click())
    getInstrumentation().waitForIdleSync()
}

fun RobotActions.clickCancelButtonOnTimePicker() {
    Espresso.onView(withId(MaterialR.id.material_timepicker_cancel_button))
        .inRoot(isDialog())
        .perform(click())
    getInstrumentation().waitForIdleSync()
}

fun RobotActions.clickModeButtonOnTimePicker() {
    Espresso.onView(withId(MaterialR.id.material_timepicker_mode_button))
        .inRoot(isDialog())
        .perform(click())
    getInstrumentation().waitForIdleSync()
}

fun RobotActions.clickView(text: CharSequence) {
    Espresso.onView(withText(text.toString())).perform(click())
}
