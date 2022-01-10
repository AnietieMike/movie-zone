package com.anietie.moviezone.utils

import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * A SingleLiveEvent used for Snackbar messages. Like a {@link SingleLiveEvent} but also prevents
 * null messages and uses a custom observer.
 * <p>
 * Note that only one observer is going to be notified of changes.
 */
class SnackBarMessage : SingleLiveEvent<Int>() {
    fun observe(owner: LifecycleOwner?, observer: SnackbarObserver) {
        super.observe(
            owner!!,
            Observer { t ->
                if (t == null) {
                    return@Observer
                }
                observer.onNewMessage(t)
            }
        )
    }

    interface SnackbarObserver {
        /**
         * Called when there is a new message to be shown.
         *
         * @param snackbarMessageResourceId The new message, non-null.
         */
        fun onNewMessage(@StringRes snackbarMessageResourceId: Int)
    }
}
