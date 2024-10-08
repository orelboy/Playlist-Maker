package com.practicum.playlist_maker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker.search.domain.models.Track

class SearchTracksAdapter(
    private var onClick: ((Track) -> Unit)? = null
) : RecyclerView.Adapter<SearchTracksViewHolder> () {
    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTracksViewHolder
    {
        return SearchTracksViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SearchTracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onClick?.invoke(tracks[position]) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}