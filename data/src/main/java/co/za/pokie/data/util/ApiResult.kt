package co.za.pokie.data.util

import retrofit2.Response
import java.net.ConnectException

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()

    data class Error(val message: String? = null) : ApiResult<Nothing>()

    data class NoInternetError(val message: String? = null) : ApiResult<Nothing>()
}

suspend inline fun <reified T> callApiClient(crossinline call: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = call()
        val responseBody = response.body()
        if (responseBody != null && response.isSuccessful) {
            ApiResult.Success(responseBody)
        } else {
            ApiResult.Error(response.message())
        }
    } catch (e: ConnectException) {
        ApiResult.NoInternetError(e.message ?: "Something went wrong with your connection")
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Something went wrong")
    }
}
