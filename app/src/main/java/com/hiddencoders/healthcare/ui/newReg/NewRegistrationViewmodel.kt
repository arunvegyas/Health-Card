package com.hiddencoders.healthcare.ui.newReg

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.hiddencoders.healthcare.data.AuthListner
import com.hiddencoders.healthcare.data.model.NewUserModel
import com.hiddencoders.healthcare.data.remote.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class NewRegistrationViewmodel @Inject constructor(val apiServices: ApiServices) : ViewModel() {
    var username: String? = ""
    var gender: Int? = 3
    var date: String? = ""
    var contactNo: String? = ""
    var adharNo: String? = ""
    var bloodGroup: String? = ""
    var cardNumer: String? = ""
    var uploadAdhar: String? = ""
    var uploadImage: String? = ""
    var location: String? = ""
    var userId: Int? = 0
    var authListner: AuthListner? = null
    private lateinit var disposable: Disposable
    private var getNewRegistraion = MutableLiveData<NewUserModel>()
    private fun isFormValid(): Boolean {
        if (username.isNullOrEmpty()) {
            authListner!!.onFailure("Enter Username")
            return false
        } else if (gender == 3) {
            authListner!!.onFailure("Please select gender")
            return false
        } else if (date.isNullOrEmpty()) {
            authListner!!.onFailure("Enter DOB")
            return false
        } else if (cardNumer.isNullOrEmpty()) {
            authListner!!.onFailure("Enter Card Number")
            return false
        } else if (contactNo.isNullOrEmpty()) {
            authListner!!.onFailure("Enter Contact No")
            return false
        } else if (contactNo!!.length < 10) {
            authListner!!.onFailure("Enter Valid No")
            return false
        } else if (location.isNullOrEmpty()) {
            authListner!!.onFailure("Enter Location")
            return false
        } else if (bloodGroup.isNullOrEmpty()) {
            authListner!!.onFailure("Enter blood Group")
            return false
        } else if (adharNo.isNullOrEmpty()) {
            authListner!!.onFailure("Enter Adhar Card No")
            return false
        } else if (adharNo!!.length < 12) {
            authListner!!.onFailure("Enter valid Adhar Card No")
            return false
        }
        return true
    }

    fun addFormToServer(): LiveData<NewUserModel> {
        if (isFormValid()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("name", username)
            jsonObject.addProperty("card_number", cardNumer)
            jsonObject.addProperty("gender", gender)
            jsonObject.addProperty("date_of_birth", date)
            jsonObject.addProperty("mobile_number", contactNo)
            jsonObject.addProperty("aadhar_number",adharNo)
            jsonObject.addProperty("blood_group", bloodGroup)
            jsonObject.addProperty("location", location)
            jsonObject.addProperty("created_by", userId)
            disposable = apiServices.addFormToServer(jsonObject).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getNewRegistraion.value = it
                    authListner!!.onSuccess()
                }, {

                })
        }
        return getNewRegistraion
    }
}