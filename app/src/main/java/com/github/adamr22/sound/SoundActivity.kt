package com.github.adamr22.sound

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R

class SoundActivity : AppCompatActivity() {
    private val SOUND_SCREEN_TITLE: String = "SOUND SCREEN TITLE"

    private lateinit var soundViewModel: SoundViewModel
    private lateinit var soundRecyclerView: RecyclerView
    private lateinit var soundScreenTitle: String
    private lateinit var ringtones: Map<String, String>
    private lateinit var toolbar: Toolbar
    private lateinit var soundAdapter: SoundRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound)
        soundViewModel = ViewModelProvider(this).get(SoundViewModel::class.java)
        soundRecyclerView = findViewById(R.id.sounds_recycler_view)
        soundScreenTitle = intent.getStringExtra(SOUND_SCREEN_TITLE).toString()
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = soundScreenTitle
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        ringtones = soundViewModel.getRingtoneMap()
        soundAdapter = SoundRecyclerViewAdapter(ringtones)
        soundRecyclerView.adapter = soundAdapter
        soundRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}