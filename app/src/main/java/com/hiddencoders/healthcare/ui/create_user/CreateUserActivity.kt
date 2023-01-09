package com.hiddencoders.healthcare.ui.create_user

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.google.gson.JsonObject
import com.hiddencoders.healthcare.BaseClass
import com.hiddencoders.healthcare.databinding.ActivityCreateUserBinding
import com.hiddencoders.healthcare.ui.utilis.Utilities
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateUserActivity : BaseClass() {
    private lateinit var binding: ActivityCreateUserBinding
    private val viewmodel by viewModels<CreateUserViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
    }

    fun setData() {
        binding.btSave.setOnClickListener {
            if (validate()) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("first_name", binding.etUsername.text.toString().trim())
                jsonObject.addProperty("last_name", binding.etLastname.text.toString().trim())
                jsonObject.addProperty("mobile", binding.etMobile.text.toString().trim())
                jsonObject.addProperty("email", binding.etEmail.text.toString().trim())
                jsonObject.addProperty("password", binding.etPassword.text.toString().trim())
                jsonObject.addProperty("role_id", "6")
                jsonObject.addProperty("created_by", userSession.getUserId())

                Handler(Looper.getMainLooper()).post {
                    viewmodel.createUser(jsonObject).observeForever {
                        if (it.status) {
                            Utilities.showAlertDialog(
                                this,
                                "Executive Registration",
                                "Executive Registered Successfully"
                            )
                            binding.etUsername.setText("")
                            binding.etLastname.setText("")
                            binding.etMobile.setText("")
                            binding.etEmail.setText("")
                            binding.etPassword.setText("")
                        } else {
                            showToast(this, it.message)
                        }
                    }
                }
            }
        }
        binding.idLogout.setOnClickListener {
            userSession.logout(this,this)
        }
    }

    private fun validate(): Boolean {
        if (binding.etUsername.text.isNullOrEmpty()) {
            showToast(this, "Enter Firstname")
            return false
        } else if (binding.etLastname.text.isNullOrEmpty()) {
            showToast(this, "Enter Lastname")
            return false
        } else if (binding.etMobile.text.isNullOrEmpty()) {
            showToast(this, "Enter Mobile Number")
            return false
        } else if (binding.etMobile.text.length < 10) {
            showToast(this, "Enter Valid Mobile Number")
            return false
        } else if (binding.etEmail.text.isNullOrEmpty()) {
            showToast(this, "Enter Email")
            return false
        } else if (binding.etPassword.text.isNullOrEmpty()) {
            showToast(this, "Enter Password")
            return false
        }
        return true
    }
}