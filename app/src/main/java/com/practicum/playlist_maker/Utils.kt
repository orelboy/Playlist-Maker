package com.practicum.playlist_maker

import android.content.Context

object Utils {
    fun Int.dpToPx(context: Context) = (this * context.resources.displayMetrics.density).toInt()
}