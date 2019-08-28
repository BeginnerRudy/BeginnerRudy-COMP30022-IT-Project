package com.example.familyRegister.core

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.util.*


/**
 * This class is responsible for the logic in date picker page
 *
 * */
class DatePickerFragment(val listener: DatePickerDialog.OnDateSetListener ): DialogFragment() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return  DatePickerDialog((activity as Context), listener, year, month, day)
    }
}