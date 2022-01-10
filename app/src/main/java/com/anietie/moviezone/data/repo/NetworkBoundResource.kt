package com.anietie.moviezone.data.repo

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.anietie.moviezone.data.local.model.Resource
import com.anietie.moviezone.data.remote.api.ApiResponse
import com.anietie.moviezone.utils.AppExecutors

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 * @param <ResultType>
 * @param <RequestType> </RequestType></ResultType>
 */
abstract class NetworkBoundResource<ResultType, RequestType>(private var mExecutors: AppExecutors) {
    /**
     * The final result LiveData
     */
    private val result: MediatorLiveData<Resource<ResultType>> =
        MediatorLiveData<Resource<ResultType>>()

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value !== newValue) {
            result.value = newValue
        }
    }

    /**
     * Fetch the data from network and persist into DB and then
     * send it back to UI.
     */
    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse: LiveData<ApiResponse<RequestType>> = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(
            dbSource,
            Observer<ResultType> { newData -> setValue(Resource.loading(newData)) }
        )
        result.addSource(
            apiResponse,
            Observer<ApiResponse<RequestType>> { response ->
                result.removeSource(apiResponse)
                result.removeSource(dbSource)
                if (response.isSuccessful) {
                    mExecutors.diskIO().execute(
                        Runnable {
                            saveCallResult(response.body!!)
                            mExecutors.mainThread()
                                .execute(
                                    Runnable { // we specially request a new live data,
                                        // otherwise we will get immediately last cached value,
                                        // which may not be updated with latest results received from network.
                                        result.addSource(
                                            loadFromDb(),
                                            Observer<ResultType> { newData ->
                                                setValue(Resource.success(newData))
                                            }
                                        )
                                    }
                                )
                        }
                    )
                } else {
                    onFetchFailed()
                    result.addSource(
                        dbSource,
                        Observer<ResultType> { newData ->
                            setValue(
                                Resource.error(response.error?.message, newData)
                            )
                        }
                    )
                }
            }
        )
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected abstract fun onFetchFailed()

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    val asLiveData: LiveData<Resource<ResultType>>
        get() = result

    init {
        // Send loading state to UI
        result.value = Resource.loading(null)
        val dbSource: LiveData<ResultType> = loadFromDb()
        result.addSource(
            dbSource,
            Observer<ResultType> { data ->
                result.removeSource(dbSource)
                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource)
                } else {
                    result.addSource(
                        dbSource,
                        Observer<ResultType> { newData -> setValue(Resource.success(newData)) }
                    )
                }
            }
        )
    }
}
