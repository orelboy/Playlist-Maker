package com.practicum.playlist_maker.utils

import android.content.Context
import com.practicum.playlist_maker.R
import java.text.SimpleDateFormat
import java.util.Locale

object AndroidUtils {
    fun Int.dpToPx(context: Context) = (this * context.resources.displayMetrics.density).toInt()
    fun Int.pxToDp(context: Context) = (this / context.resources.displayMetrics.density).toInt()

    fun tracksCountString(context: Context, tracksCount: Int?): String {

        val trackPost =
            when (tracksCount?.rem(10)) {
                1 -> context.getString(R.string.track)
                in 2..4 ->context.getString(R.string.two_track)
                else -> context.getString(R.string.five_track)
            }

        return "$tracksCount $trackPost"
    }

    fun trackDurationString(context: Context, duration: Long): String {
        val minutes = Math.round((duration / 60000).toDouble())
        val post: String = when (minutes % 100) {
            in 11..14 -> context.getString(R.string.minutes_genitive)
            else -> when (minutes % 10) {
                1L -> context.getString(R.string.minute)
                in 2..4 -> context.getString(R.string.minutes)
                else -> context.getString(R.string.minutes_genitive)
            }
        }
        return "$minutes $post"
    }

    fun trackDurationToTimeString(duration: Long): String {

        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)

    }
}