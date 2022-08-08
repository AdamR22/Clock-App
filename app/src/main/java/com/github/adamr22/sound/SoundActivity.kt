package com.github.adamr22.sound

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R

class SoundActivity : AppCompatActivity() {
    private val SOUND_SCREEN_TITLE: String = "SOUND SCREEN TITLE"

    private val TAG = "SoundActivity"

    private lateinit var soundViewModel: SoundViewModel
    private lateinit var soundRecyclerView: RecyclerView
    private lateinit var soundScreenTitle: String
    private var ringtones: Map<String, String> = mapOf()
    private lateinit var toolbar: Toolbar
    private lateinit var soundAdapter: SoundRecyclerViewAdapter

    private fun hasReadExternalStoragePermission() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound)
        requestPermission()
        soundViewModel = ViewModelProvider(this).get(SoundViewModel::class.java)
        soundRecyclerView = findViewById(R.id.sounds_recycler_view)
        soundScreenTitle = intent.getStringExtra(SOUND_SCREEN_TITLE).toString()
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = soundScreenTitle
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        soundAdapter = SoundRecyclerViewAdapter(ringtones)
        soundRecyclerView.adapter = soundAdapter
        soundRecyclerView.layoutManager = LinearLayoutManager(this)
        Log.d(TAG, "onCreate: Ringtone List size: ${ringtones.size}")
    }

    private fun requestPermission() {
        val permissionsArray = mutableListOf<String>()
        if (!hasReadExternalStoragePermission()) {
            permissionsArray.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissionsArray.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0 && permissions.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED && permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE) {
                    ringtones = soundViewModel.getRingtoneMap()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}