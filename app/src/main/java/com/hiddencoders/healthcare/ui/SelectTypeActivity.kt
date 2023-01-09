package com.hiddencoders.healthcare.ui

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.hiddencoders.healthcare.BaseClass
import com.hiddencoders.healthcare.R
import com.hiddencoders.healthcare.databinding.SelectTypeActivityBinding
import com.hiddencoders.healthcare.ui.newReg.NewRegistrationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectTypeActivity : BaseClass() {
    private lateinit var binding: SelectTypeActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelectTypeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
    }

    private fun setData() {
        Glide.with(this).load(R.drawable.logo_final).into(binding.ivLogo)
        binding.btNew.setOnClickListener {
            startActivity(Intent(this, NewRegistrationActivity::class.java))
        }
        binding.btExisting.setOnClickListener {
            startActivity(Intent(this, ExisitingUsersActivity::class.java))
        }
        binding.idLogout.setOnClickListener {
            userSession.logout(this,this)
        }
    }
}