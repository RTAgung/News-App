package com.example.newsapp.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Helper {
    fun getDateFor7DaysAgo(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val newDate: Date = calendar.time
        return dateFormat.format(newDate)
    }

    fun changeDateFormat(date: String, format: String = "yyyy-MM-dd"): String {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(date)
        val output = SimpleDateFormat(format, Locale.getDefault()).format(input)
        return output.toString()
    }

    fun getToolbarHeight(context: Context?): Int? {
        val styledAttributes =
            context?.theme?.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val toolbarHeight = styledAttributes?.getDimension(0, 0f)?.toInt()
        styledAttributes?.recycle()
        return toolbarHeight
    }
}