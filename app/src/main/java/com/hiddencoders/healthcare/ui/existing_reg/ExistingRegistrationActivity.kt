package com.hiddencoders.healthcare.ui.existing_reg

import android.R
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.viewModels
import com.google.gson.Gson
import com.hiddencoders.healthcare.BaseClass
import com.hiddencoders.healthcare.data.AuthListner
import com.hiddencoders.healthcare.data.model.NewUserData
import com.hiddencoders.healthcare.data.model.NewUserModel
import com.hiddencoders.healthcare.databinding.ActivityExistingRegBinding
import com.hiddencoders.healthcare.ui.utilis.Utilities
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ExistingRegistrationActivity : BaseClass(), AuthListner {
    private lateinit var binding: ActivityExistingRegBinding
    private val viewmodel by viewModels<ExistingRegistrationViewmodel>()
    private lateinit var data: NewUserData
    var mSelectedMonth: String = ""
    var mSelectedDay: String = ""
    var mSelectedYear: String = ""
    var mDOB: String = ""
    private var mYear: Int = 0
    private var mMOnth_val: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExistingRegBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null) {
            val gson = Gson()
            data = gson.fromJson(intent.getStringExtra("data"), NewUserData::class.java)
        }
        viewmodel.authListner = this
        viewmodel.username = data.name
        viewmodel.adharNo = data.aadhar_number
        viewmodel.date = data.date_of_birth
        viewmodel.bloodGroup = data.blood_group
        viewmodel.gender = data.gender.toInt()
        viewmodel.cardNumer = data.card_number
        viewmodel.location = data.location
        viewmodel.userId = userSession.getUserId().toInt()
        viewmodel.contactNo = data.mobile_number
        viewmodel.cardNumer = data.id

        binding.etDate.setText(data.date_of_birth)
        binding.etUsername.setText(data.name)
        binding.etAdharNo.setText(data.aadhar_number)
        binding.etCardNo.setText(data.card_number)
        binding.etContactNo.setText(data.mobile_number)
        binding.etLocation.setText(data.location)

        binding.etGender.setOnClickListener {
            openGenderDialog()
        }
        binding.etDate.setOnClickListener {
            openDateDialog()
        }
        val list = arrayOf(
            "--Select Blood Group--",
            "A+ve",
            "O+ve",
            "B+ve",
            "O-ve",
            "A-ve",
            "B-ve",
            "AB+ve"
        )

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spBloodGroup.adapter = adapter
        binding.spBloodGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewmodel.bloodGroup = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        for (i in list.indices) {
            val group = list[i]
            if (group == data.blood_group) {
                binding.spBloodGroup.setSelection(i, true)
            }
        }

        if (data.gender == "0") {
            binding.etGender.setText("Male")
        } else if (data.gender == "1") {
            binding.etGender.setText("Female")
        } else if (data.gender == "2") {
            binding.etGender.setText("Others")
        }
        binding.btSave.setOnClickListener {
            viewmodel.adharNo = binding.etAdharNo.text.toString()
            viewmodel.contactNo = binding.etContactNo.text.toString()
            viewmodel.username = binding.etUsername.text.toString()
            viewmodel.cardNumer = binding.etCardNo.text.toString()
            viewmodel.location = binding.etLocation.text.toString()
            viewmodel.addFormToServer()
        }
    }

    private fun openDateDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(com.hiddencoders.healthcare.R.layout.datedialog)
        val picker_dailog =
            dialog.findViewById(com.hiddencoders.healthcare.R.id.picker_dicker) as DatePicker
        val body = dialog.findViewById(com.hiddencoders.healthcare.R.id.tvTitle) as TextView
        body.text = "Enter From Date"
        val ok_dialog = dialog.findViewById(com.hiddencoders.healthcare.R.id.tvOk) as TextView
        val cancel_dialog =
            dialog.findViewById(com.hiddencoders.healthcare.R.id.tvCancel) as TextView
        val c = Calendar.getInstance()
        c[2003, 8] = 1
        val ca = Calendar.getInstance()
        ca.add(Calendar.YEAR, -19)
        picker_dailog.maxDate = ca.timeInMillis
        val calender = Calendar.getInstance()
        mYear = calender[Calendar.YEAR]
        val mMonth = calender[Calendar.MONTH]
        var selectedYear: String = ""
        val mDay = calender[Calendar.DAY_OF_MONTH]
        picker_dailog.init(mYear, mMonth, mDay,
            DatePicker.OnDateChangedListener { view, year, month, day ->

                mSelectedYear = year.toString()

                if (month < 9) {
                    val mMonth = month + 1
                    mSelectedMonth = "0$mMonth"
                }
                if (month >= 9) {
                    val mMonth = month + 1
                    mSelectedMonth = mMonth.toString()
                }

                if (day < 10) {
                    mSelectedDay = "0$day"
                }
                if (day >= 10) {
                    mSelectedDay = day.toString()
                }
                mDOB = "$mSelectedYear-$mSelectedMonth-$mSelectedDay"
                viewmodel.date = mDOB
                binding.etDate.setText(mDOB)

            })

        if (binding.etDate.text!!.isNotEmpty()) {
            val mYearEdit = binding.etDate.text.toString().trim()
            val items1: List<String> = mYearEdit.split("-")
            val date1 = items1[0]
            mMOnth_val = items1[1].toInt()
            val year = items1[2]
            val mMonth_selected = mMOnth_val - 1

            picker_dailog.updateDate(
                Integer.parseInt(date1),
                mMonth_selected,
                Integer.parseInt(year)
            );
        }

        ok_dialog.setOnClickListener {

            dialog.dismiss()

        }
        cancel_dialog.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    private fun openGenderDialog() {
        val view = layoutInflater.inflate(com.hiddencoders.healthcare.R.layout.dialog_gender, null)
        val dialog =
            Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()

        val tvMale = dialog.findViewById<TextView>(com.hiddencoders.healthcare.R.id.tvMale)
        val tvFemale = dialog.findViewById<TextView>(com.hiddencoders.healthcare.R.id.tvFemale)
        val tvOthers = dialog.findViewById<TextView>(com.hiddencoders.healthcare.R.id.tvOthers)

        tvMale.setOnClickListener {
            binding.etGender.setText("Male")
            viewmodel.gender = 0
            dialog.dismiss()
        }
        tvFemale.setOnClickListener {
            binding.etGender.setText("Female")
            viewmodel.gender = 1
            dialog.dismiss()
        }
        tvOthers.setOnClickListener {
            binding.etGender.setText("Others")
            viewmodel.gender = 2
            dialog.dismiss()
        }

    }

    override fun onStarted() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        binding.progressbar.visibility = View.GONE
        Utilities.showAlertDialog(this, "Update Card", "Added Successfully")
        Handler().postDelayed({
            onBackPressed()
        }, 3000)
    }

    override fun onFailure(message: String) {
        showToast(this, message)
    }
}