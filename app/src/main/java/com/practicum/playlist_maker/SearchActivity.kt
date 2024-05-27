package com.practicum.playlist_maker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible

private const val SEARCH_TEXT = "SEARCH_TEXT"

class SearchActivity : AppCompatActivity() {
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<ImageView>(R.id.back)
        val imageClickListener: View.OnClickListener = View.OnClickListener { finish() }
        buttonBack.setOnClickListener(imageClickListener)

        val editText = findViewById<EditText>(R.id.edittext_in_search_string)
        val btnClear = findViewById<ImageView>(R.id.icon_clear_in_string)
        editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClear.isVisible = editText.text.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = editText.text.toString()
            }
        })

        fun closeKeyboard(){
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = this.currentFocus ?: View(this)
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        btnClear.setOnClickListener{
            editText.text.clear()
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