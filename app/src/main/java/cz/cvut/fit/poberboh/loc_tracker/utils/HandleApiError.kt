package cz.cvut.fit.poberboh.loc_tracker.utils

import androidx.fragment.app.Fragment
import cz.cvut.fit.poberboh.loc_tracker.network.Resource

fun Fragment.handleApiError(error: Resource.Error, retry: (() -> Unit)? = null) {
    when {
        error.isNetworkError -> requireView().snackbar(
            "Please check your internet connection",
            retry
        )

        else -> requireView().snackbar(error.errorBody?.string().toString())
    }
}
