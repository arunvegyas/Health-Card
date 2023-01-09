package com.hiddencoders.healthcare.ui.newReg

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.viewModels
import com.hiddencoders.healthcare.BaseClass
import com.hiddencoders.healthcare.R
import com.hiddencoders.healthcare.data.AuthListner
import com.hiddencoders.healthcare.databinding.ActivityNewRegBinding
import com.hiddencoders.healthcare.ui.utilis.Utilities
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NewRegistrationActivity : BaseClass(),AuthListner {

    private lateinit var binding: ActivityNewRegBinding
    private val viewmodel by viewModels<NewRegistrationViewmodel>()
    var mSelectedMonth: String = ""
    var mSelectedDay: String = ""
    var mSelectedYear: String = ""
    var mDOB: String = ""
    private var mYear: Int = 0
    private var mMOnth_val: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewRegBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
    }

    private fun setData() {
        viewmodel.authListner = this
        viewmodel.userId = userSession.getUserId().toInt()
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spBloodGroup.adapter = adapter
        binding.spBloodGroup.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewmodel.bloodGroup=parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        binding.btSave.setOnClickListener {
            viewmodel.adharNo = binding.etAdharNo.text.toString()
            viewmodel.contactNo = binding.etContactNo.text.toString()
            viewmodel.username = binding.etUsername.text.toString()
            viewmodel.cardNumer = binding.etCardNo.text.toString()
            viewmodel.location = binding.etLocation.text.toString()
            viewmodel.addFormToServer()
        }
        binding.btCancel.setOnClickListener {
            onBackPressed()
        }
        binding.ivImage1.setOnClickListener {

        }
        binding.ivImage2.setOnClickListener {

        }
    }

    private fun openGenderDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_gender, null)
        val dialog =
            Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()

        val tvMale = dialog.findViewById<TextView>(R.id.tvMale)
        val tvFemale = dialog.findViewById<TextView>(R.id.tvFemale)
        val tvOthers = dialog.findViewById<TextView>(R.id.tvOthers)

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

    private fun openDateDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.datedialog)
        val picker_dailog = dialog.findViewById(R.id.picker_dicker) as DatePicker
        val body = dialog.findViewById(R.id.tvTitle) as TextView
        body.text = "Enter From Date"
        val ok_dialog = dialog.findViewById(R.id.tvOk) as TextView
        val cancel_dialog = dialog.findViewById(R.id.tvCancel) as TextView
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

    override fun onStarted() {

    }

    override fun onSuccess() {
        Utilities.showAlertDialog(this,"Tagging","Added Successfully")
        Handler().postDelayed({
            onBackPressed()
        },3000)
    }

    override fun onFailure(message: String) {
       showToast(this,message)
    }

}