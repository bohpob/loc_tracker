package cz.cvut.fit.poberboh.loc_tracker.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let { snackbar.setAction("Retry") { it() } }
    snackbar.show()
}