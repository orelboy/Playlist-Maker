package com.practicum.playlist_maker.utils

import android.content.Context

object AndroidUtils {
    fun Int.dpToPx(context: Context) = (this * context.resources.displayMetrics.density).toInt()
}