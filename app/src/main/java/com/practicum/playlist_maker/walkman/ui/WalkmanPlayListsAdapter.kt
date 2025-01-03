package com.practicum.playlist_maker.walkman.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist

class WalkmanPlayListsAdapter(
    private var onClick: ((Playlist) -> Unit)? = null
) : RecyclerView.Adapter<WalkmanPlayListsViewHolder>() {
    var playlists = arrayListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkmanPlayListsViewHolder
    {
        return WalkmanPlayListsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: WalkmanPlayListsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onClick?.invoke(playlists[position]) }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}
