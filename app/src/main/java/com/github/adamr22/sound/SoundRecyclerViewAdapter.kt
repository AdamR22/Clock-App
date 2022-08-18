package com.github.adamr22.sound

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import java.lang.Exception

class SoundRecyclerViewAdapter(
    private val ringtones: List<Pair<String, Uri>>,
    private val mediaPlayer: MediaPlayer,
    private val alarmViewModel: AlarmViewModel,
    private val alarmItemIndex: Int,
    private val alarmToneTitle: String,
    private val context: Context,
) : RecyclerView.Adapter<SoundViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sound_item_layout, parent, false)
        return SoundViewHolder(
            view,
            ringtones,
            mediaPlayer,
            alarmViewModel,
            alarmItemIndex,
            context
        )
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        holder.ringtoneTitle.text = ringtones[position].first
        holder.selectedIcon.visibility =
            if (holder.selectedItemPosition == position || (holder.ringtoneTitle.text == alarmToneTitle && holder.selectedItemPosition == position)) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return ringtones.size
    }
}

class SoundViewHolder(
    itemView: View,
    ringtones: List<Pair<String, Uri>>,
    mediaPlayer: MediaPlayer,
    alarmViewModel: AlarmViewModel,
    alarmItemIndex: Int,
    context: Context
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var ringtoneTitle: TextView
    var selectedIcon: ImageView
    var selectedItemPosition: Int = 0
    private val mMediaPlayer = mediaPlayer
    private val mRingtones = ringtones
    private val mAlarmViewModel = alarmViewModel
    private val mAlarmItemIndex = alarmItemIndex
    private val mContext = context

    private val TAG = "SoundViewHolder"

    init {
        ringtoneTitle = itemView.findViewById(R.id.tv_ringtone_title)
        selectedIcon = itemView.findViewById(R.id.chosen_sound)
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        selectedItemPosition = adapterPosition
        setAlarmTune(mAlarmItemIndex)
        playTune(mRingtones[selectedItemPosition].second)
    }

    private fun setAlarmTune(index: Int) {
        mAlarmViewModel.changeRingtone(
            index,
            mRingtones[index].first,
            mRingtones[index].second,
        )
    }

    private fun playTune(uri: Uri) {
        try {
            mMediaPlayer.reset()
            mMediaPlayer.setDataSource(mContext, uri)
            mMediaPlayer.isLooping = false
            mMediaPlayer.prepare()
            mMediaPlayer.start()
        } catch (e: Exception) {
            Log.d(TAG, "playTune: ${e.stackTrace}")
        }
    }
}