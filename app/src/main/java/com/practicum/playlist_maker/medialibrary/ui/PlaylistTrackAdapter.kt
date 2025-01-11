package com.practicum.playlist_maker.medialibrary.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.search.ui.SearchTracksViewHolder

class PlaylistTrackAdapter (
    private var onClick: ((Track) -> Unit)? = null ,
    private var onLongClick: ((Track) -> Unit)? = null

) : RecyclerView.Adapter<SearchTracksViewHolder> () {
    val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTracksViewHolder
    {
        return SearchTracksViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SearchTracksViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener { onClick?.invoke(tracks[position]) }

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(tracks[position])
            true
        }

    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}