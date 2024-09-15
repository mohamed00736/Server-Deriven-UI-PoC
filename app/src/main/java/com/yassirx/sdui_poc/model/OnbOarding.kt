package com.yassirx.sdui_poc.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class OnboardingData(
    val groups: List<OnboardingGroup>
)

data class OnboardingGroup(
    val id: Int,
    val title: String,
    val pages: List<OnboardingPage>
) {
    val progress: Float
        get() {
            val total = pages.sumOf { it.components.size }.toFloat()
            var completed = 0f
            pages.forEach { page ->
                page.components.forEach { component ->
                    if (!component.value.isNullOrBlank()) completed++
                }
            }
            return completed / total
        }

    val hasRejectedItems: Boolean get() {
        pages.forEach { page ->
            page.components.forEach { item ->
                if (item.isRejected) return true
            }
        }
        return false
    }

    val isApproved: Boolean get() {
        pages.forEach { page ->
            page.components.forEach { item ->
                if (!item.isAccepted) return false
            }
        }
        return true
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val components: List<OnboardingComponent>
) {
    val canPassToNextPage: Boolean get() {
        components.forEach {
            if (it.mandatory && it.value.isNullOrBlank()) return false
        }
        return true
    }
}

data class OnboardingComponent(
    val type: String,
    val title: String,
    var value: String? = null,
    var displayedValue: String? = null,
    val key: String,
    var status: String? = null,
    var message: String? = null,
    val mandatory: Boolean,
   // val path: String?,
    val options: List<String>? = null,
    @SerializedName("path") val uploadPath: String? = null
) {
    enum class ComponentStatus(val value: String) {
        ACCEPTED("approved"),
        REJECTED("rejected"),
        PENDING("pending"),
    }

    val enabled: Boolean get() = !(status == ComponentStatus.ACCEPTED.value || status == ComponentStatus.PENDING.value)

    val isRejected: Boolean get() = status == ComponentStatus.REJECTED.value

    val isPending: Boolean get() = status == ComponentStatus.PENDING.value

    val isAccepted: Boolean get() = status == ComponentStatus.ACCEPTED.value
}

data class FileUploadedRes(
    @Json(name = "data") val data: FileUploadedData
)

data class FileUploadedData(
    @Json(name = "mediaID") val id: Int?,
    @Json(name = "url") val url: String?,
)
