package com.practicum.playlist_maker.recyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.Utils.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class SearchTracksViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
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
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTime)
    }
}