package com.yassirx.sdui_poc.data.network

import com.google.gson.annotations.SerializedName

data class ErrorApiResponse(
    @SerializedName("status_code") val statusCode: Int?,
    @SerializedName("status_message") val statusMessage: String?,
    @SerializedName("success") val success: Boolean?,
)