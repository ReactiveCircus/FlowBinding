package reactivecircus.flowbinding.testing

import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View

fun motionEventAtPosition(view: View, action: Int, xPercent: Int, yPercent: Int): MotionEvent {
    val paddingLeft = view.paddingLeft
    val paddingRight = view.paddingRight
    val paddingTop = view.paddingTop
    val paddingBottom = view.paddingBottom

    val width = view.width
    val height = view.height

    val topLeft = IntArray(2)
    view.getLocationInWindow(topLeft)
    val x1 = topLeft[0] + paddingLeft
    val y1 = topLeft[1] + paddingTop
    val x2 = x1 + width - paddingLeft - paddingRight
    val y2 = y1 + height - paddingTop - paddingBottom

    val x = x1 + (x2 - x1) * xPercent / 100f
    val y = y1 + (y2 - y1) * yPercent / 100f

    val time = SystemClock.uptimeMillis()
    return MotionEvent.obtain(time, time, action, x, y, 0)
}

fun hoverMotionEventAtPosition(view: View, action: Int, xPercent: Int, yPercent: Int): MotionEvent {
    val event = motionEventAtPosition(view, action, xPercent, yPercent)

    val pointerProperties = arrayOf(MotionEvent.PointerProperties())

    val pointerCoordinates = arrayOf(MotionEvent.PointerCoords()).apply {
        get(0).x = event.x
        get(0).y = event.y
    }

    return MotionEvent.obtain(
        event.downTime, event.eventTime,
        event.action, 1, pointerProperties, pointerCoordinates, event.metaState, 0,
        event.xPrecision, event.yPrecision, event.deviceId, event.edgeFlags,
        InputDevice.SOURCE_CLASS_POINTER, event.flags
    )
}
