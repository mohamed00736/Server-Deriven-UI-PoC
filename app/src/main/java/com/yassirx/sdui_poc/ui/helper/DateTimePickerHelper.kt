package com.yassirx.sdui_poc.ui.helper

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import com.yassirx.sdui_poc.toNiceIntDisplay
import java.util.Calendar


@Composable
fun dateTimePicker(
    context: Context,
    dateTimeSeparator: String = "T",
    onDateTimeSelected:(String) -> Unit,
): DatePickerDialog {
    val c = Calendar.getInstance()
    val picker = DatePickerDialog(
        context,
        { _, year, month, day ->
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                context,
                {_, hour : Int, minute: Int ->
                    onDateTimeSelected("${day.toNiceIntDisplay()}/${month.toNiceIntDisplay()}/$year" +
                            dateTimeSeparator +
                            "${hour.toNiceIntDisplay()}:${minute.toNiceIntDisplay()}"
                    )
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        },
        c.get(Calendar.YEAR),
        c.get(Calendar.MONTH),
        c.get(Calendar.DAY_OF_MONTH),
    )
    picker.apply {
        datePicker.minDate = System.currentTimeMillis()
        datePicker.maxDate = System.currentTimeMillis().plus(1000 * 60 * 60 * 24 * 20)
    }
    return picker
}

@Composable
fun datePicker(
    context: Context,
    onDateSelected:(String) -> Unit,
): DatePickerDialog {
    val c = Calendar.getInstance()
    val picker = DatePickerDialog(
        context,
        { _, year, month, day ->
            onDateSelected("${day.toNiceIntDisplay()}/${(month.plus(1)).toNiceIntDisplay()}/$year")
        },
        c.get(Calendar.YEAR),
        c.get(Calendar.MONTH),
        c.get(Calendar.DAY_OF_MONTH),
    )
    return picker
}

@Composable
fun timePicker(
    context: Context,
    onTimeSelected:(String) -> Unit,
): TimePickerDialog {
    val calendar = Calendar.getInstance()
    val picker = TimePickerDialog(
        context,
        {_, hour : Int, minute: Int ->
            onTimeSelected("${hour.toNiceIntDisplay()}:${minute.toNiceIntDisplay()}")
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )
    return picker
}



@Composable
fun datePicker(
    context: Context,
    mindate: Long? = null,
    maxdate: Long? = null,
    onDateSelected: (String) -> Unit,
): DatePickerDialog {
    val c = Calendar.getInstance()
    val picker = DatePickerDialog(
        context,
        { _, year, month, day ->
            onDateSelected("${day.toNiceIntDisplay()}/${(month.plus(1)).toNiceIntDisplay()}/$year")
        },
        c.get(Calendar.YEAR),
        c.get(Calendar.MONTH),
        c.get(Calendar.DAY_OF_MONTH),
    )
    picker.apply {
        mindate?.let { datePicker.minDate = it }
        datePicker.maxDate = maxdate ?: System.currentTimeMillis().plus(1000 * 60 * 60 * 24 * 20)
    }
    return picker
}

@Composable
fun timePicker(
    context: Context,
    extraHour: Int? = null,
    onTimeSelected: (String) -> Unit,
): TimePickerDialog {
    val calendar = Calendar.getInstance()
    extraHour?.let { calendar.add(Calendar.HOUR_OF_DAY , it) }
    val picker = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            onTimeSelected("${hour.toNiceIntDisplay()}:${minute.toNiceIntDisplay()}")
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )
    return picker
}