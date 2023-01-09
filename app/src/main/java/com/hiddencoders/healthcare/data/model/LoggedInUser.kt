package com.hiddencoders.healthcare.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val usemessage: String,
    val http_code: Int,
    val status: Boolean,
    val data: UserData
)

data class UserData(
    val id: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val mobile: String,
    val token:String
)