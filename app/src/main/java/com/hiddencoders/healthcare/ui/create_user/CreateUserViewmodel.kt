package com.hiddencoders.healthcare.ui.create_user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.hiddencoders.healthcare.data.model.ExecutiveModel
import com.hiddencoders.healthcare.data.remote.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CreateUserViewmodel @Inject constructor(val apiServices: ApiServices) : ViewModel() {
    private lateinit var disposable: Disposable
    private var getExecutiveResponse = MutableLiveData<ExecutiveModel>()

    fun createUser(jsonObject: JsonObject): LiveData<ExecutiveModel> {
        disposable = apiServices.createUser(jsonObject).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getExecutiveResponse.value = it
            }, {
                Log.d("TAG", "createUser: ")
            })
        return getExecutiveResponse
    }
}