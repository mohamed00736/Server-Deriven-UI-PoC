package com.yassirx.sdui_poc.data.repo


import com.yassirx.sdui_poc.data.network.UserApi
import com.yassirx.sdui_poc.data.network.SafeApiCall
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApi
) : SafeApiCall {

//    suspend fun getOnBoarding() = safeApiCall { api.getOnBoarding() }
//
//    suspend fun uploadFile(
//        path: String,
//        data: RequestBody,
//    ) = safeApiCall {
//        api.uploadFile(path = path, file = data)
//    }


    suspend fun getOnBoardingStatus() = safeApiCall(apiCall = api::getOnBoardingStatus)

    suspend fun createNewOnBoardingApplication(body: Map<String, Any?>) = safeApiCall {
        api.createNewOnBoardingApplication(body)
    }

    suspend fun patchNewOnBoardingApplication(body: Map<String, Any?>) = safeApiCall {
        api.patchOnBoardingApplication(body)
    }

    suspend fun fetchImageById(id: String) = safeApiCall { api.fetchImageById(id) }
}