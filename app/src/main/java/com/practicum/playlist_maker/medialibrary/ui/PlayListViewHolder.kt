package com.practicum.playlist_maker.medialibrary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.utils.AndroidUtils.dpToPx
import com.practicum.playlist_maker.utils.AndroidUtils.tracksCountString

class PlayListViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_item, parent, false) ) {

    private val coverView: ImageView = itemView.findViewById(R.id.cover)
    private val nameView: TextView = itemView.findViewById(R.id.name)
    private val tracksCount: TextView = itemView.findViewById(R.id.tracks_count)

    fun bind(playlist: Playlist) {
        nameView.text = playlist.name
        tracksCount.text = playlist.tracksCount?.let { tracksCountString(itemView.context, it) }

        Glide.with(itemView)
            .load(playlist.coverPathUri)
            .placeholder(R.drawable.ic_placeholder_312)
            .transform(CenterCrop(),RoundedCorners(8.dpToPx(itemView.context.applicationContext)))
            .into(coverView)
    }
}