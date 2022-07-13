package com.github.adamr22.sound

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R

class SoundRecyclerViewAdapter(
    private val ringtones: Map<String, String>,
) : RecyclerView.Adapter<SoundViewHolder>() {

    private val ringtoneTitles = ArrayList<String>()
    private val ringtoneUris = ArrayList<String>()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ringtones.forEach { (title, uri) ->
                ringtoneTitles.add(title)
                ringtoneUris.add(uri)
            }
        } else {
            ringtones.keys.forEach { title ->
                ringtoneTitles.add(title)
            }

            ringtones.values.forEach { uri ->
                ringtoneUris.add(uri)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sound_item_layout, parent, false)
        return SoundViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        holder.selectedIcon.visibility = if (holder.selectedItemPosition == position) View.VISIBLE else View.GONE
        holder.ringtoneTitle.text = ringtoneTitles[position]
    }

    override fun getItemCount(): Int {
        return ringtones.size
    }
}

class SoundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var ringtoneTitle: TextView
    var selectedIcon: ImageView
    var selectedItemPosition: Int = 0

    init {
        ringtoneTitle = itemView.findViewById(R.id.tv_ringtone_title)
        selectedIcon = itemView.findViewById(R.id.chosen_sound)
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        selectedItemPosition = adapterPosition
        // TODO: function to play and set selected ringtone as alarm tone
    }
}