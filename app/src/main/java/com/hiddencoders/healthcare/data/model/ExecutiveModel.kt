package com.hiddencoders.healthcare.data.model

data class ExecutiveModel(
    val message: String,
    val http_code: Int,
    val status: Boolean,
    val data: ExecutiveData
)

data class ExecutiveData(val id: String)
