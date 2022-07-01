package com.app.utility
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.roundToInt

fun AppCompatActivity.getRootView(): View {
//    return findViewById(android.R.id.content)
    return window.findViewById(Window.ID_ANDROID_CONTENT)
}
fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}
fun AppCompatActivity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = this.convertDpToPx(50F).roundToInt()
    return heightDiff > marginOfError
}

fun AppCompatActivity.isKeyboardOpened(): Boolean {
    /*val softKeyboardHeight = 80
    val r = Rect()
    getRootView().getWindowVisibleDisplayFrame(r)
    val dm: DisplayMetrics = getRootView().resources.displayMetrics
    val heightDiff: Int = getRootView().bottom - r.bottom
    return heightDiff > softKeyboardHeight * dm.density*/


//    return this.isKeyboardOpen()
    return true
}


fun AppCompatActivity.getRootViewKeyboard(): Boolean {
    val insets = ViewCompat.getRootWindowInsets(getRootView()) ?: return false
    return insets.isVisible(WindowInsetsCompat.Type.ime())
}



