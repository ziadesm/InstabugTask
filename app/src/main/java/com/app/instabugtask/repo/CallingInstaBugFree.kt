package com.app.instabugtask.repo
import com.app.database.DatabaseHelperSingleton
import com.app.network.RequestHandler
import com.app.network_helper.NetworkResponse

class CallingInstaBugFree constructor(
    private val db: DatabaseHelperSingleton,
    private val api: RequestHandler,
) {

    fun callInstaBugWebsite(url: String): NetworkResponse<String, Throwable> {
        return try {
            when(val response = api.requestGET(url)) {
                is NetworkResponse.Success -> {
                    db.insertWordsToDatabase(response.body)
                    NetworkResponse.Success(response.body)
                }
                is NetworkResponse.ApiError -> {
                    NetworkResponse.ApiError(Throwable(response.body), response.code)
                }
                else -> NetworkResponse.ApiError(Throwable("Unknown Response"), -1)
            }
        } catch (e: Throwable) {
            NetworkResponse.ApiError(e, 0)
        }
    }
}