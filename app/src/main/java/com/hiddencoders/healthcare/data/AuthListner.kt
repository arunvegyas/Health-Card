package com.hiddencoders.healthcare.data

interface AuthListner {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}