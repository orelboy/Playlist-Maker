package com.practicum.playlist_maker.walkman.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.utils.AndroidUtils.dpToPx
import com.practicum.playlist_maker.utils.AndroidUtils.tracksCountString


class WalkmanPlayListsViewHolder(
    parent: ViewGroup,
):
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item_in_bottomsheet, parent, false) ) {

    private val coverView: ImageView = itemView.findViewById(R.id.cover)
    private val nameView: TextView = itemView.findViewById(R.id.playlistName)
    private val tracksCountView: TextView = itemView.findViewById(R.id.tracks_count)

    fun bind(playlist: Playlist) {
        Glide.with(itemView)
            .load(playlist.coverPathUri)
            .placeholder(R.drawable.placeholder_ic_album)
            .centerCrop()
            .transform(RoundedCorners(2.dpToPx(itemView.context.applicationContext)))
            .into(coverView)

        nameView.text = playlist.name
        tracksCountView.text = playlist.tracksCount?.let {
            tracksCountString(itemView.context,
                it
            )
        }
    }
}
