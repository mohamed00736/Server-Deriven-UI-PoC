package com.arbin.arbincommon.ui.components

import com.yassirx.sdui_poc.ui.components.TextFieldState


enum class GenderEnum {
    Male, Female, Other
}
class GenderStateState :
    TextFieldState(validator = ::isGenderValid, errorFor = ::phoneNumberValidationError)

class ConfirmGenderStateState(private val GenderStateState: GenderStateState) : TextFieldState() {
    override val isValid
        get() = genderAndConfirmationValid(GenderStateState.text, text)

    override fun getError(): String? {
        return if (showErrors()) {
            phoneNumberConfirmationError()
        } else {
            null
        }
    }
}

private fun genderAndConfirmationValid(gender: String, confirmedGender: String): Boolean {
    return isGenderValid(gender)
}

private fun isGenderValid(phoneNumber: String): Boolean {
    return phoneNumber.isNotEmpty()
}

@Suppress("UNUSED_PARAMETER")
private fun phoneNumberValidationError(phoneNumber: String): String {
    return "Invalid phoneNumber"
}

private fun phoneNumberConfirmationError(): String {
    return "PhoneNumbers don't match"
}
