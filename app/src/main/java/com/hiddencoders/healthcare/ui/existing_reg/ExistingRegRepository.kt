package com.hiddencoders.healthcare.ui.existing_reg

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiddencoders.healthcare.data.model.NewUserModel
import com.hiddencoders.healthcare.data.remote.ApiServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class ExistingRegRepository @Inject constructor(val apiServices: ApiServices) {
    private lateinit var disposable: Disposable
    private var getExistingResponse = MutableLiveData<NewUserModel>()

    fun getUserData(mobile: String, adhar: String): LiveData<NewUserModel> {
        disposable = apiServices.getUserDetails(mobile, adhar).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getExistingResponse.value = it
            }, {
                Log.d("TAG", "getUserData: ")
            })
        return getExistingResponse
    }
}