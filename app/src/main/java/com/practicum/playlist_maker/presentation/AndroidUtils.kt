package com.practicum.playlist_maker.presentation

import android.content.Context

object AndroidUtils {
    fun Int.dpToPx(context: Context) = (this * context.resources.displayMetrics.density).toInt()
}