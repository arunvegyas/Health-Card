package com.hiddencoders.healthcare.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.hiddencoders.healthcare.BaseClass
import com.hiddencoders.healthcare.databinding.ActivityLoginBinding

import com.hiddencoders.healthcare.R
import com.hiddencoders.healthcare.data.remote.ApiServices
import com.hiddencoders.healthcare.ui.SelectTypeActivity
import com.hiddencoders.healthcare.ui.create_user.CreateUserActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseClass() {

    @Inject
    lateinit var apiServices: ApiServices
    private lateinit var binding: ActivityLoginBinding
    private lateinit var disposable: Disposable
    private lateinit var adminDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        Glide.with(this).load(R.drawable.logo_final).into(binding.ivLogo)
        val list = arrayOf("Executive", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCenterName.adapter = adapter
        binding.spCenterName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        login.setOnClickListener {
            if (isValidData()) {
                if (binding.spCenterName.selectedItem.toString() == "Admin"){
                    checkAdminLogin(username.text.toString(), password.text.toString())
                } else{
                    checkLogin(username.text.toString(), password.text.toString())
                }

            }
        }
    }
    private fun checkAdminLogin(username: String, password: String) {
        binding.progressbar.visibility = View.VISIBLE
        val jsonObject = JsonObject()
        jsonObject.addProperty("email", username)
        jsonObject.addProperty("password", password)
        adminDisposable = apiServices.verifyAdminLogin(jsonObject).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.progressbar.visibility = View.GONE
                if (it.status) {
                    userSession.setAccessToken(it.data.token)
                    userSession.setUsername(
                        it.data.first_name + " " + it
                            .data.last_name
                    )
                    userSession.setUserId(it.data.id)
                    userSession.setDeviceId("6")
                    updateUiWithUser(it.data.first_name)
                } else {
                    showToast(this, "Invalid login details")
                }
            }, {
                binding.progressbar.visibility = View.GONE
                showToast(this,"User login failed")
                Log.d("TAG", "checkLogin: ")
            })
    }
    private fun checkLogin(username: String, password: String) {
        binding.progressbar.visibility = View.VISIBLE
        val jsonObject = JsonObject()
        jsonObject.addProperty("email", username)
        jsonObject.addProperty("password", password)
        disposable = apiServices.verifyLogin(jsonObject).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.progressbar.visibility = View.GONE
                if (it.status) {
                    userSession.setAccessToken(it.data.token)
                    userSession.setUsername(
                        it.data.first_name + " " + it
                            .data.last_name
                    )
                    userSession.setUserId(it.data.id)
                    updateUiWithUser(it.data.first_name)
                } else {
                    showToast(this, "Invalid login details")
                }
            }, {
                binding.progressbar.visibility = View.GONE
                showToast(this,"User login failed")
            })
    }

    private fun isValidData(): Boolean {
        if (binding.username.text.isEmpty()) {
            binding.username.error = "Empty fields not allowed"
            return false
        } else if (binding.password.text.isEmpty()) {
            binding.password.error = "Empty fields not allowed"
            return false
        }
        return true
    }

    private fun updateUiWithUser(firstName: String) {
        val welcome = getString(R.string.welcome)
        val displayName = firstName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        if (binding.spCenterName.selectedItem.toString() == "Admin"){
            startActivity(Intent(this, CreateUserActivity::class.java))
            finish()
        } else{
            startActivity(Intent(this, SelectTypeActivity::class.java))
            finish()
        }

    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
