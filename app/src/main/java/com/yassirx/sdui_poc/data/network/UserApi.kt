package com.yassirx.sdui_poc.data.network


import com.harbin.vtcdrivertransport.model.OnBoardingStatus
import okhttp3.RequestBody
import retrofit2.http.*


interface UserApi {


//    @GET("driver/onBoardings")
//    suspend fun getOnBoarding(): OnBoarding
//
//
//    @POST("driver/{path}")
//    suspend fun uploadFile(
//        @Path("path", encoded = true) path: String,
//        @Body file: RequestBody,
//    ): FileUploadedRes


    @GET("driver/application/status")
    suspend fun getOnBoardingStatus(): OnBoardingStatus

    @POST("driver/application")
    @JvmSuppressWildcards
    suspend fun createNewOnBoardingApplication(@Body body: Map<String, Any?>)

    @PATCH("driver/application")
    @JvmSuppressWildcards
    suspend fun patchOnBoardingApplication(@Body body: Map<String, Any?>)

    @GET("driver/applications/media/{id}")
    suspend fun fetchImageById(
        @Path("id") id: String,
    ): String
}