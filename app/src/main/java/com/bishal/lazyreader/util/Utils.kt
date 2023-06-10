package com.bishal.lazyreader.util

import android.icu.text.DateFormat
import java.sql.Timestamp

fun formatDate(timestamp: Timestamp): String {
    return DateFormat.getDateInstance().format(timestamp).toString().split(",")[0] // March 12
}