package com.harbin.vtcdrivertransport.model


import com.squareup.moshi.Json
import com.yassirx.sdui_poc.toStandardDateDisplay


data class OnBoardingStatus(
    @Json(name = "driver_application") val driverApplication: DriverApplication?,
    @Json(name = "vehicle_application") val vehicleApplication: VehicleApplication?,
    @Json(name = "vehicle_fields_status") val vehicleFieldsStatus: List<FieldsStatus>,
    @Json(name = "driver_fields_status") val driverFieldsStatus: List<FieldsStatus>,
)

data class DriverApplication(
    @Json(name = "id") val id: Int?,
    @Json(name = "profile_photo_id") val profilePhotoId: Int?,
    @Json(name = "license_photo_front_id") val licensePhotoFrontId: Int?,
    @Json(name = "license_photo_back_id") val licensePhotoBackId: Int?,
    @Json(name = "license_expiration_date") val licenseExpirationDate: String?,
    @Json(name = "birth_date") val birthDate: String?,
    @Json(name = "email") val email: String?,
    @Json(name = "first_name") val firstName: String?,
    @Json(name = "last_name") val lastName: String?,
    @Json(name = "recruited_by") val recruitedBy: String?,
    @Json(name = "gender") val gender: String?,
    @Json(name = "status") val status: String?
){
    fun toMap() : Map<String, Any?> = hashMapOf(
        "id" to id,
        "profile_photo_id" to profilePhotoId,
        "license_photo_front_id" to licensePhotoFrontId,
        "license_photo_back_id" to licensePhotoBackId,
        "license_expiration_date" to licenseExpirationDate?.toStandardDateDisplay(dateTimeSeparator = "T"),
        "birth_date" to birthDate?.toStandardDateDisplay(dateTimeSeparator = "T"),
        "email" to email,
        "first_name" to firstName,
        "last_name" to lastName,
        "recruited_by" to recruitedBy,
        "gender" to gender,
        "status" to status
    )
}

data class FieldsStatus(
    @Json(name = "id") val id: Int?,
    @Json(name = "status") val status: String,
    @Json(name = "field") private val _field: String,
    @Json(name = "reason") val reason: String?
) {
    private val fieldMapper = hashMapOf<String, String?>(
        "license_expiration" to "license_expiration_date",
        "control_technique_front_id" to "control_technique_media_front_id",
        "insurance_front_id" to "insurance_media_front_id",
        "insurance_expiration" to "insurance_expiration_date",
        "registration_front_id" to "registration_media_front_id",
        "car_media" to "car_media_id",
    )
    val field: String get() {
        return fieldMapper[_field] ?: _field
    }
}

data class VehicleApplication(
    @Json(name = "id") val id: Int?,
    @Json(name = "licence_plate") val licencePlate: String?,
    @Json(name = "care_make_id") val careMakeId: Int?,
    @Json(name = "car_model_id") val carModelId: Int?,
    @Json(name = "control_technique_media_front_id") val controlTechniqueMediaFrontId: Int?,
    @Json(name = "control_technique_media_back_id") val controlTechniqueMediaBackId: Int?,
    @Json(name = "insurance_media_front_id") val insuranceMediaFrontId: Int?,
    @Json(name = "insurance_media_back_id") val insuranceMediaBackId: Int?,
    @Json(name = "insurance_expiration_date") val insuranceExpirationDate: String?,
    @Json(name = "registration_media_front_id") val registrationMediaFrontId: Int?,
    @Json(name = "registration_media_back_id") val registrationMediaBackId: Int?,
    @Json(name = "car_media_id") val carMediaId: Int?,
    @Json(name = "poa_media_front_id") val poaMediaFrontId: Int?,
    @Json(name = "poa_media_back_id") val poaMediaBackId: Int?,
    @Json(name = "status") val status: String?,
) {
    fun toMap(): Map<String, Any?> = hashMapOf(
        "id" to id,
        "licence_plate" to licencePlate,
        "care_make_id" to careMakeId,
        "car_model_id" to carModelId,
        "control_technique_media_front_id" to controlTechniqueMediaFrontId,
        "control_technique_media_back_id" to controlTechniqueMediaBackId,
        "insurance_media_front_id" to insuranceMediaFrontId,
        "insurance_media_back_id" to insuranceMediaBackId,
        "insurance_expiration_date" to insuranceExpirationDate?.toStandardDateDisplay(dateTimeSeparator = "T"),
        "registration_media_front_id" to registrationMediaFrontId,
        "registration_media_back_id" to registrationMediaBackId,
        "car_media_id" to carMediaId,
        "poa_media_front_id" to poaMediaFrontId,
        "poa_media_back_id" to poaMediaBackId,
        "status" to status
    )
}