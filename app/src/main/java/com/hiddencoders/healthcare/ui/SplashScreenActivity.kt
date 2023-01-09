package com.hiddencoders.healthcare.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.hiddencoders.healthcare.BaseClass
import com.hiddencoders.healthcare.R
import com.hiddencoders.healthcare.databinding.ActivitySplashBinding
import com.hiddencoders.healthcare.ui.create_user.CreateUserActivity
import com.hiddencoders.healthcare.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : BaseClass() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(R.drawable.logo_final).into(binding.ivLogo)
        Handler().postDelayed({
            if (userSession.getAccessToken().isNotEmpty()){
                if (userSession.getDeviceId().isNotEmpty()){
                    startActivity(Intent(this,CreateUserActivity::class.java))
                    finish()
                } else{
                    startActivity(Intent(this,SelectTypeActivity::class.java))
                    finish()
                }

            } else{
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        },3000)
    }
}