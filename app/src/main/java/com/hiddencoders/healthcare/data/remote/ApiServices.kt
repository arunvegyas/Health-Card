package com.hiddencoders.healthcare.data.remote

import com.google.gson.JsonObject
import com.hiddencoders.healthcare.data.model.ExecutiveModel
import com.hiddencoders.healthcare.data.model.LoggedInUser
import com.hiddencoders.healthcare.data.model.NewUserModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiServices {
    @POST("executive/login")
    fun verifyLogin(@Body jsonObject: JsonObject): Single<LoggedInUser>

    @POST("admin/login")
    fun verifyAdminLogin(@Body jsonObject: JsonObject): Single<LoggedInUser>

    @POST("card/create")
    fun addFormToServer(@Body jsonObject: JsonObject): Single<NewUserModel>

    @POST("executive/create")
    fun createUser(@Body jsonObject: JsonObject): Single<ExecutiveModel>

    @PUT("card/update")
    fun updateFormToServer(@Body jsonObject: JsonObject): Single<NewUserModel>

    @GET("card/get")
    fun getUserDetails(
        @Query("mobile_number") mobile_number: String,
        @Query("aadhar_number") aadhar_number: String
    ): Single<NewUserModel>
}