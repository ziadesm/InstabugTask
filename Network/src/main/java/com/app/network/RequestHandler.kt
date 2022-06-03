package com.app.network
import com.app.network_helper.NetworkResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RequestHandler {
    companion object {
        const val GET : String = "GET"

        private var instance: RequestHandler? = null
        fun getInstance(): RequestHandler {
            synchronized(RequestHandler::class.java) {
                if (instance == null) {
                    instance = RequestHandler()
                }
            }
            return instance!!
        }
    }

    fun requestGET(url: String): NetworkResponse<String, Throwable> {
        val obj = URL(url)
        val con = obj.openConnection() as HttpURLConnection
        con.requestMethod = GET
        val responseCode = con.responseCode

        return if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
            val `in` =
                BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String?
            val response = StringBuffer()
            while (`in`.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            `in`.close()
            NetworkResponse.Success(response.toString())
        } else {
            val `in` =
                BufferedReader(InputStreamReader(con.errorStream))
            var inputLine: String?
            val response = StringBuffer()
            while (`in`.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            `in`.close()
            NetworkResponse.ApiError(Throwable(response.toString()), responseCode)
        }
    }
}