package com.practicum.playlist_maker.main.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.practicum.playlist_maker.databinding.ActivityMainBinding
import com.practicum.playlist_maker.medialibrary.ui.MediaLibraryActivity
import com.practicum.playlist_maker.search.ui.SearchActivity
import com.practicum.playlist_maker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

//Реализация анонимного класса
        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displayIntent = Intent( v?.context , SearchActivity::class.java)
                startActivity(displayIntent)
            }
        }
        binding.search.setOnClickListener(imageClickListener)

//Лямбда-выражение
        binding.mediaLibrary.setOnClickListener {
            val displayIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(displayIntent)
        }
        binding.settings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}