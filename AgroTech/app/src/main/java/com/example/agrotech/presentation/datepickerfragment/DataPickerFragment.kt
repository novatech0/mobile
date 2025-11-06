package com.example.agrotech.presentation.datepickerfragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePickerDialogFragment(private val onDateSet: (String) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): DatePickerDialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-${selectedDay}"
                onDateSet(selectedDate)  // Pass the selected date to the callback
            },
            year,
            month,
            day
        )
    }
}

