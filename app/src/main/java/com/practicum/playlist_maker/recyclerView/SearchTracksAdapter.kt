package com.practicum.playlist_maker.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker.R

class SearchTracksAdapter : RecyclerView.Adapter<SearchTracksViewHolder> () {
    var tracks = ArrayList<Track>()
    private var onClick: ((Track) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_track, parent, false)
        return SearchTracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onClick?.invoke(tracks[position]) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
    fun setOnItemClickListener(listener: (Track) -> Unit) {
        onClick = listener
    }
}