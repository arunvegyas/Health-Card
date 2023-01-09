package com.hiddencoders.healthcare.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.hiddencoders.healthcare.BaseClass
import com.hiddencoders.healthcare.R
import com.hiddencoders.healthcare.databinding.ActivitySearchBinding
import com.hiddencoders.healthcare.ui.existing_reg.ExistingRegistrationActivity
import com.hiddencoders.healthcare.ui.existing_reg.ExistingRegistrationViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExisitingUsersActivity : BaseClass() {
    private lateinit var binding: ActivitySearchBinding
    private val viewmodel by viewModels<ExistingRegistrationViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(R.drawable.logo_final).into(binding.ivLogo)
        binding.btNew.setOnClickListener {
            if (binding.etCardNo.text.length > 9) {
                if (binding.etCardNo.text.length == 10) {
                    viewmodel.getUserData(binding.etCardNo.text.toString(), "").observeForever {
                        if (it.status) {
                            if (it.data != null) {
                                val gson = Gson()
                                startActivity(
                                    Intent(this, ExistingRegistrationActivity::class.java).putExtra(
                                        "data", gson.toJson(
                                            it.data
                                        )
                                    )
                                )
                            } else {

                            }

                        } else{
                            showToast(this,it.message)
                        }
                    }

                } else if (binding.etCardNo.text.length == 12) {
                    viewmodel.getUserData("", binding.etCardNo.text.toString()).observeForever {
                        if (it.status) {
                            if (it.data != null) {
                                val gson = Gson()
                                startActivity(
                                    Intent(this, ExistingRegistrationActivity::class.java).putExtra(
                                        "data", gson.toJson(
                                            it.data
                                        )
                                    )
                                )
                            } else {

                            }
                        } else{
                            showToast(this,it.message)
                        }
                    }

                } else {
                    showToast(this, "Enter Valid Number")
                }
            } else {
                showToast(this, "Enter Valid Number")
            }
        }
    }
}