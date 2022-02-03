package com.chema.siemprellegastarde.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.chema.siemprellegastarde.activities.EdicionnActivity
import java.util.*


class TimePickerFragment (val editText: EditText) : DialogFragment(),
    TimePickerDialog.OnTimeSetListener {

    private var listenerT: TimePickerDialog.OnTimeSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c: Calendar = Calendar.getInstance()
        val hour: Int = c.get(Calendar.HOUR_OF_DAY)
        val minute: Int = c.get(Calendar.MINUTE)

        // Create a new instance of DatePickerDialog and return it
        return TimePickerDialog(requireActivity(), this, hour, minute, is24HourFormat(context))
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val hora = String.format("%02d", hourOfDay)
        val minuto = String.format("%02d", minute + 1)
        editText!!.setText("$hora:$minuto")
    }


}