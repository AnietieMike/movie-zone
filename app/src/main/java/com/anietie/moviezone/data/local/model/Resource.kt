package com.anietie.moviezone.data.local.model

import java.util.Objects

/**
 * A generic class that contains data and status about loading this data.
 *
 * You can read more about it in the [Architecture Guide]
 * ([https://developer.android.com/jetpack/docs/guide]).
 */
class Resource<T> private constructor(
    val status: Status,
    val data: T?,
    val message: String?
) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Resource<*>) return false
        return status == o.status &&
            data == o.data &&
            message == o.message
    }

    override fun hashCode(): Int {
        return Objects.hash(status, data, message)
    }

    /**
     * Status of a resource that is provided to the UI.
     *
     * These are usually created by the Repository classes where they return
     * `LiveData<Resource></Resource><T>>` to pass back the latest data to the UI with its fetch status.
     </T> */
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        /**
         * Creates [Resource] object with `SUCCESS` status and [data].
         */
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        /**
         * Creates [Resource] object with `ERROR` status and [message].
         */
        fun <T> error(msg: String?, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        /**
         * Creates [Resource] object with `LOADING` status to notify
         * the UI to show loading.
         */
        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}
