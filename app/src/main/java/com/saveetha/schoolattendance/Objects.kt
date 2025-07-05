package com.saveetha.schoolattendance

import android.content.Context
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Objects {

    val currentDate: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }

    fun showProgressDialog(context:Context): AlertDialog{
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_progress)  // use a custom layout or system one
        builder.setCancelable(false)
        var progressDialog: AlertDialog = builder.create()
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)
        progressDialog.show()
        return progressDialog
    }

}