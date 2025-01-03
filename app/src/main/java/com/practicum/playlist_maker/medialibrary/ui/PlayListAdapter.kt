package com.practicum.playlist_maker.medialibrary.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist

class PlayListAdapter (
    private var onClick: ((Playlist) -> Unit)? = null,
) : RecyclerView.Adapter<PlayListViewHolder>() {

    var playlists = arrayListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        return PlayListViewHolder(parent)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onClick?.invoke(playlists[position])}
    }

}