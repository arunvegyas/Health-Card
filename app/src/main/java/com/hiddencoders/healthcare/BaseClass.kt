package com.hiddencoders.healthcare

import android.content.Context
import android.os.Environment
import android.text.format.DateFormat
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hiddencoders.healthcare.ui.utilis.sharedPreferences.UserSession
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject


open class BaseClass : AppCompatActivity() {

    @Inject
    lateinit var userSession: UserSession

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val nomeFoto = DateFormat.format("yyyy-MM-dd_hhmmss", Date()).toString()
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(nomeFoto, ".jpg", storageDir)
        val pictureFilePath = image.absolutePath
        return image
    }
    fun showToast(
        context: Context,
        message: String?
    ) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}