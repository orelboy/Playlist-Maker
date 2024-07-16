package com.practicum.playlist_maker.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.practicum.playlist_maker.R

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//Реализация анонимного класса
        val buttonSearch = findViewById<Button>(R.id.search)
        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displayIntent = Intent( v?.context , SearchActivity::class.java)
                startActivity(displayIntent)
            }
        }
        buttonSearch.setOnClickListener(imageClickListener)
//Лямбда-выражение
        val buttonMediaLibrary = findViewById<Button>(R.id.media_library)
        val buttonSettings = findViewById<Button>(R.id.settings)
        buttonMediaLibrary.setOnClickListener {
            val displayIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(displayIntent)
        }
        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}