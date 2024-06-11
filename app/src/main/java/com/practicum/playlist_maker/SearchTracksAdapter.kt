package com.practicum.playlist_maker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchTracksAdapter  (
    private val data: List<Track>
) : RecyclerView.Adapter<SearchTracksViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_track, parent, false)
        return SearchTracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTracksViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}