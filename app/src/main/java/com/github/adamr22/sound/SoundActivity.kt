package com.github.adamr22.sound

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class SoundActivity : AppCompatActivity() {

    private val TAG = "SoundActivity"

    private val VIEW_MODEL = "Alarm View Model"
    private val PERMISSION_REQUEST_CODE = 0
    private val ALARM_ITEM_INDEX = "Alarm Item Index"
    private val ALARM_TONE_TITLE = "Alarm Tone Title"

    private lateinit var soundViewModel: SoundViewModel
    private lateinit var soundRecyclerView: RecyclerView
    private lateinit var soundRecyclerViewAdapter: SoundRecyclerViewAdapter
    private lateinit var soundScreenTitle: String
    private lateinit var toolbar: Toolbar

    private val ringtoneList: MutableList<Pair<String, Uri>> = mutableListOf()

    private lateinit var ringtoneManager: RingtoneManager
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioAttributes: AudioAttributes
    private lateinit var alarmViewModel: AlarmViewModel


    private fun hasReadExternalStoragePermission() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound)
        alarmViewModel = intent?.getParcelableExtra(VIEW_MODEL)!!
        val alarmItemIndex = intent?.getIntExtra(ALARM_ITEM_INDEX, 0)!!
        val alarmToneTitle = intent?.getStringExtra(ALARM_TONE_TITLE)!!
        setUpMediaPlayer()
        requestPermission()
        soundViewModel = ViewModelProvider(this).get(SoundViewModel::class.java)
        soundRecyclerViewAdapter = SoundRecyclerViewAdapter(
            ringtoneList,
            mediaPlayer,
            alarmViewModel,
            alarmItemIndex,
            alarmToneTitle,
            this
        )
        soundRecyclerView = findViewById(R.id.sounds_recycler_view)
        soundRecyclerView.adapter = soundRecyclerViewAdapter
        soundScreenTitle = intent.extras?.get(RingtoneManager.EXTRA_RINGTONE_TITLE).toString()
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = soundScreenTitle
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        soundRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun requestPermission() {
        val permissionsArray = mutableListOf<String>()
        if (!hasReadExternalStoragePermission()) {
            permissionsArray.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(
                this,
                permissionsArray.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE && permissions.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED && permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE) {
                    setUpRingtones()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setUpRingtones() {
        ringtoneManager = RingtoneManager(this).also {
            it.setType((intent.extras)?.get(RingtoneManager.EXTRA_RINGTONE_TYPE) as Int)
        }
        val cursor = ringtoneManager.cursor
        lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
                if (cursor.moveToFirst()) {
                    while (cursor.moveToNext()) {
                        val id = cursor.getInt(RingtoneManager.ID_COLUMN_INDEX)
                        val uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)
                        val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                        ringtoneList.add(Pair(title, Uri.parse("$uri/$id")))
                    }
                }
            }
        }
    }

    private fun setUpMediaPlayer() {
        mediaPlayer = MediaPlayer()
        audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        mediaPlayer.setAudioAttributes(audioAttributes)
    }

    override fun onPause() {
        super.onPause()
        try {
            mediaPlayer.stop()
        } catch (e: Exception) {
            Log.d(TAG, "onPause: ${e.stackTrace}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mediaPlayer.release()
        } catch (e: Exception) {
            Log.d(TAG, "onDestroy: ${e.printStackTrace()}")
        }
    }
}