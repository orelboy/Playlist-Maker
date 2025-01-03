package com.practicum.playlist_maker.utils

import android.content.Context
import com.practicum.playlist_maker.R

object AndroidUtils {
    fun Int.dpToPx(context: Context) = (this * context.resources.displayMetrics.density).toInt()

    fun tracksCountString(context: Context, tracksCount: Int): String {
        val trackPost =
            when (tracksCount % 10) {
                1 -> context.getString(R.string.track)
                in 2..4 ->context.getString(R.string.two_track)
                else -> context.getString(R.string.five_track)
            }
        return "$tracksCount $trackPost"
    }
}