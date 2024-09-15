package com.yassirx.sdui_poc

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


inline fun <reified T: Any> Context.fromJsonFile(raw: Int): T? {
    return try {
        val json = resources.openRawResource(raw).bufferedReader().use {
            it.readText()
        }
        json.fromJson()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

inline fun <reified T: Any> String.fromJson(): T? {
    return try {
        Gson().fromJson(this, object: TypeToken<T>() {}.type)
    } catch (e: JsonSyntaxException){
        e.printStackTrace()
        null
    }
}
fun Any.toJson(): String? {
    try {
        return Gson().toJson(this)
    } catch (e: Exception) {
        e.stackTrace
    }
    return null
}

fun Context.findActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }
fun Double.toNiceFormat(
    aroundUp: Int = 2,
    hardDecimal: Boolean = false
): String{
    return if (this > this.toInt() || hardDecimal) String.format("%.${aroundUp}f", this)
    else String.format("%.0f", this)
}

fun Int.toNiceIntDisplay(): String = if (this >= 10) this.toString() else "0$this"


@SuppressLint("DiscouragedApi")
fun String.localize(context: Context): String {
    val resources = context.resources
    val packageName = context.packageName
    val resourceId = resources.getIdentifier(this, "string", packageName)
    return if (resourceId != 0) {
        resources.getString(resourceId)
    } else this
}


fun String.toNiceDate(
    withYear: Boolean = true,
    withTime: Boolean = true,
    withDayOfWeek: Boolean = false,
    today: String = "Today",
    yesterday: String = "Yesterday",
    pattern: String = "yyyy-MM-dd",
    dateTimeSeparator: String = " - ",
    hourRange: Int? = null,
): String{
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val data = this.split("T")
            val date = LocalDate.parse(
                data[0],
                DateTimeFormatter.ofPattern(pattern)
            )
            val currentDate = LocalDate.now()
            if (!withTime){
                if (currentDate.year == date.year && currentDate.month == date.month){
                    if (currentDate.dayOfMonth == date.dayOfMonth) return today
                    else if (currentDate.dayOfMonth.minus(1) == date.dayOfMonth) return yesterday
                }
            }
            val displayDate = "${
                if (withDayOfWeek) date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()).toCapitalize() + " " else ""
            }${date.dayOfMonth} ${
                date.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).toCapitalize()
            } ${if (withYear) date.year else ""}"
            if (withTime) {
                val time = data[1].split(":")
                "$displayDate$dateTimeSeparator${time[0]}:${time[1]}${
                    hourRange?.let { range ->
                        " - ${time[0].toInt().plus(range).toNiceIntDisplay()}:${time[1]}"
                    } ?: ""
                }"
            }
            else displayDate
        } else this
    }catch (e: Exception){
        e.printStackTrace()
        this
    }
}

fun String.toCapitalize(): String{
    return this.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}

fun String.toStandardDateDisplay(
    withTime: Boolean = true,
    dateTimeSeparator: String = " ",
): String{
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val data = this.split("T")
            val date = LocalDate.parse(
                data[0] ,
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val displayDate = "${date.dayOfMonth.toNiceIntDisplay()}/${date.monthValue.toNiceIntDisplay()}/${date.year}"
            if (withTime) {
                val time = data[1].split(":")
                "$displayDate$dateTimeSeparator${time[0]}:${time[1]}"
            }
            else displayDate
        } else this
    }catch (e: Exception){
        this
    }
}

fun Modifier.dashedBorder(
    strokeWidth: Dp,
    color: Color,
    cornerRadiusDp: Dp
) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }
        val cornerRadiusPx = density.run { cornerRadiusDp.toPx() }

        this.then(
            Modifier.drawWithCache {
                onDrawBehind {
                    val stroke = Stroke(
                        width = strokeWidthPx,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    drawRoundRect(
                        color = color,
                        style = stroke,
                        cornerRadius = CornerRadius(cornerRadiusPx)
                    )
                }
            }
        )
    }
)


fun String.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidLicensePlate() = this.matches(Regex("^\\d{5,6}-1\\d{2}-\\d{2}\$"))
