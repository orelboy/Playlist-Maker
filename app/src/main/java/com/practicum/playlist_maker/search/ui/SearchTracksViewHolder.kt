package com.practicum.playlist_maker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.creator.AndroidUtils.dpToPx
import com.practicum.playlist_maker.search.domain.models.Track

class SearchTracksViewHolder (
    parent: ViewGroup,
    ):
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
        .inflate(R.layout.music_track, parent, false) ){

    private val albumPicture: ImageView = itemView.findViewById(R.id.albumPicture)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(item: Track) {
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder_ic_album)
            .centerCrop()
            .transform(RoundedCorners(2.dpToPx(itemView.context.applicationContext)))
            .into(albumPicture)

        trackName.text = item.trackName
        artistName.text = item.artistName
        artistName.requestLayout()
        trackTime.text = item.getTrackTime(item.trackTime)
    }
}