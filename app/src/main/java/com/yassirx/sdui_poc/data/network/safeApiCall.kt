package com.yassirx.sdui_poc.data.network


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        val gson = Gson()
                        val responseType = object : TypeToken<ErrorApiResponse>() {}.type
                        val errorResponse = throwable.response()?.errorBody()?.string()

                        val apiError = try {
                            errorResponse?.let { error ->
                                gson.fromJson<ErrorApiResponse>(
                                    error,
                                    responseType
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }

                        Resource.Failure(
                            false,
                            apiError?.statusCode ?: 400,
                            apiError?.statusMessage ?: "Something went wrong",
                            throwable.response()?.errorBody()
                        )
                    }

                    else -> {
                        println(throwable)
                        Resource.Failure(true, 0, "Network exception", null)
                    }
                }
            }
        }
    }
}