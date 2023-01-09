package com.hiddencoders.healthcare.data.model

data class NewUserModel(
    val status: Boolean,
    val http_code: Int,
    val message: String,
    val data: NewUserData
)

data class NewUserData(
    val id: String,
    val card_number: String,
    val name: String,
    val gender: String,
    val date_of_birth: String,
    val mobile_number: String,
    val aadhar_number: String,
    val blood_group: String,
    val location: String,
    val profile_pic: String,
    val aadhar_pic: String,
    val created_by: String
)
