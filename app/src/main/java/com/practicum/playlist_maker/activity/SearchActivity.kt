package com.practicum.playlist_maker.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.recyclerView.Track
import com.practicum.playlist_maker.api.ITunesSearchAPI
import com.practicum.playlist_maker.api.SearchTracksResponse
import com.practicum.playlist_maker.recyclerView.SearchTracksAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SEARCH_TEXT = "SEARCH_TEXT"
class SearchActivity : AppCompatActivity() {
    private var searchText = ""
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesSearchService = retrofit.create(ITunesSearchAPI::class.java)

    private lateinit var querySearchString: EditText
    private lateinit var rvTracksList: RecyclerView

    private val tracks = ArrayList<Track>()
    private val adapter = SearchTracksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<ImageView>(R.id.back)
        val imageClickListener: View.OnClickListener = View.OnClickListener { finish() }
        buttonBack.setOnClickListener(imageClickListener)

        adapter.tracks=tracks

        querySearchString = findViewById(R.id.edittext_in_search_string)
        rvTracksList = findViewById(R.id.rvTracks)

        rvTracksList.adapter = adapter

        val btnClear = findViewById<ImageView>(R.id.icon_clear_in_string)
        val btnUpdate = findViewById<Button>(R.id.updateButton)
        val errorInternet = findViewById<LinearLayout>(R.id.errorInternet)
        val errorSearch = findViewById<LinearLayout>(R.id.errorSearch)

        fun closeKeyboard(){
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val view = this.currentFocus ?: View(this)
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun search(){
            if (querySearchString.text.isNotEmpty()) {
                iTunesSearchService.search("song", querySearchString.text.toString())
                .enqueue(object :
                    Callback<SearchTracksResponse> {
                    override fun onResponse(
                        call: Call<SearchTracksResponse>,
                        response: Response<SearchTracksResponse>
                    ) {
                        if (response.code() == 200) {
                            rvTracksList.isVisible = true
                            errorInternet.isVisible = false
                            errorSearch.isVisible = false
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            } else {
                                errorSearch.isVisible = true
                                errorInternet.isVisible = false
                                rvTracksList.isVisible = false
                            }
                        } else {
                            errorInternet.isVisible = true
                            errorSearch.isVisible = false
                            rvTracksList.isVisible = false
                        }
                    }

                    override fun onFailure(
                        call: Call<SearchTracksResponse>,
                        t: Throwable
                    ) {
                        errorInternet.isVisible = true
                        errorSearch.isVisible = false
                        rvTracksList.isVisible = false
                    }
                })
            }
        }

        querySearchString.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClear.isVisible = querySearchString.text.isNotEmpty()
                if (btnClear.isVisible == false){
                    rvTracksList.isVisible = false
                    errorInternet.isVisible = false
                    errorSearch.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = querySearchString.text.toString()
            }
        })

        querySearchString.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
        btnUpdate.setOnClickListener {
            search()
        }

        btnClear.setOnClickListener{
            querySearchString.text.clear()
            rvTracksList.isVisible = false
            errorInternet.isVisible = false
            errorSearch.isVisible = false
            closeKeyboard()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
            searchText = savedInstanceState.getString(SEARCH_TEXT,"")
        if (searchText.isNotEmpty()){
            val editText = findViewById<EditText>(R.id.edittext_in_search_string)
            editText.setText(searchText)
            editText.setSelection(editText.length())
        }
    }
}