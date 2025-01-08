package com.practicum.playlist_maker.medialibrary.domain.models

import android.content.Context
import android.net.Uri
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.utils.AndroidUtils.trackDurationToTimeString
import com.practicum.playlist_maker.utils.AndroidUtils.tracksCountString

class PlaylistDetailedInfo(
    val id: Int,
    val name: String,
    val description: String?,
    val tracksCount: Int?,
    val coverPathUri: Uri?,
    val tracks: List<Track>,
) {

    var totalDuration: Long = 0L

    init {

        if (tracks.isNotEmpty()) {
            totalDuration = tracks.map { it.trackTime }.sum()
        }
    }

    fun mapToString(context: Context): String {

        val builder = StringBuilder()
        builder.append(name)
        if (!description.isNullOrEmpty()) {
            builder.append("\n")
            builder.append(description)
        }
        builder.append("\n")
        builder.append(tracksCount?.let { tracksCountString(context, it) })


        tracks.forEachIndexed { index, track ->
            builder.append("\n")
            builder.append(
                "${index + 1}.${track.artistName} - ${track.trackName} (${
                    trackDurationToTimeString(
                        track.trackTime
                    )
                })"
            )


        }

        return builder.toString()

    }


}