package com.practicum.playlist_maker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist_maker.recyclerView.Track

private const val MAX_COUNT = 10

class SearchHistory(private val sharedPrefs: SharedPreferences)  {

    private var historyListTracks: ArrayList<Track> = getHistory()

    fun getHistory(): ArrayList<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }

    private fun setHistory(historyList: ArrayList<Track>){
        val json = Gson().toJson(historyList)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun addTrack(track: Track) {
        val index = historyListTracks.indexOfFirst { it.trackId == track.trackId }
        if (index != -1) {
            historyListTracks.removeAt(index)
        }

        if (historyListTracks.size >= MAX_COUNT) {
            historyListTracks.removeLast()
        }

        historyListTracks.add(0, track)
        setHistory(historyListTracks)

    }

    fun clearHistory() {
        sharedPrefs
            .edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    fun getList(): ArrayList<Track> {
        return historyListTracks
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "key_search_history"
    }
}