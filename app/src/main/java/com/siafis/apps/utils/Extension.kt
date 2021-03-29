package com.siafis.apps.utils

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.siafis.apps.R
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun String.isEmailValid(): Boolean {
    val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(this)
    return matcher.matches()
}

fun View.snackBar(message: String?) {
    Snackbar.make(this, message!!, Snackbar.LENGTH_SHORT).show()
}

fun TextInputLayout.inputError(data: String, message: String?): Boolean {
    return if (data.isEmpty()) {
        this.error = message
        false
    } else {
        this.error = null
        true
    }
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

val String.formattedDateDay: String
    get() = LocalDate.parse(
        this,
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    ).format(
        DateTimeFormatter.ofPattern("dd MMMM YYYY")
    )

fun String.toTimeStamp(): Date? {
    val formatter: DateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.parse(this)
}

fun Long.timeToDate(): String {
    val sdf = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
    val netDate = Date(this)
    return sdf.format(netDate)
}


fun TextInputEditText.clearInput(
    inputLayout: TextInputLayout
) {
    this.setOnFocusChangeListener { _, hasFoccus ->
        if (hasFoccus) {
            inputLayout.error = null
        }
    }
}

fun TextInputEditText.openCalender(
    context: Context?
) {
    val myCalendar = Calendar.getInstance()
    val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalendar[Calendar.YEAR] = year
        myCalendar[Calendar.MONTH] = monthOfYear
        myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        this.setText(sdf.format(myCalendar.time))
    }
    this.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            val dialog = DatePickerDialog(
                context!!,
                R.style.DatePickerTheme,
                date,
                myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }
    this.setOnClickListener {
        val dialog = DatePickerDialog(
            context!!,
            R.style.DatePickerTheme,
            date,
            myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}

fun String.firstWordCapitalize(): String {
    var result = ""
    split(" ").forEach {
        result += if (it.isNotEmpty() && it.isNotBlank()) {
            "${it[0].toUpperCase()}${it.substring(1).toLowerCase(Locale.ROOT)} "
        } else {
            ""
        }
    }
    return result
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
