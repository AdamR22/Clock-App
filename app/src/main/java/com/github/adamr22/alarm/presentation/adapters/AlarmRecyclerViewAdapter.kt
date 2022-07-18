package com.github.adamr22.alarm.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.alarm.data.models.AlarmItemModel
import com.github.adamr22.common.AddLabelDialog
import com.github.adamr22.common.TimePicker
import com.github.adamr22.common.VibrateSingleton
import com.github.adamr22.sound.SoundActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class AlarmRecyclerViewAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<AlarmRecyclerViewAdapter.AlarmItemViewHolder>() {

    private var data = ArrayList<AlarmItemModel>()

    private var mExpandedPosition: Int = -1
    private lateinit var mRecyclerView: RecyclerView
    private val SOUND_SCREEN_TITLE: String = "SOUND SCREEN TITLE"

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    inner class AlarmItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addLabel: TextView
        val expandOrCollapseItem: ImageButton
        val currentTime: TextView
        val alarmSchedule: TextView
        val activateAlarm: SwitchMaterial
        val btnMonday: ToggleButton
        val btnTuesday: ToggleButton
        val btnWednesday: ToggleButton
        val btnThursday: ToggleButton
        val btnFriday: ToggleButton
        val btnSaturday: ToggleButton
        val btnSunday: ToggleButton
        val selectSong: TextView
        val delete: TextView
        val vibrate: CheckBox
        val extraContent: LinearLayout

        init {
            addLabel = itemView.findViewById(R.id.tv_label)
            expandOrCollapseItem = itemView.findViewById(R.id.expand_or_collapse_alarm_item)
            currentTime = itemView.findViewById(R.id.set_time)
            alarmSchedule = itemView.findViewById(R.id.tv_days_scheduled)
            activateAlarm = itemView.findViewById(R.id.switch_alarm_on_off)
            btnMonday = itemView.findViewById(R.id.mon)
            btnTuesday = itemView.findViewById(R.id.tue)
            btnWednesday = itemView.findViewById(R.id.wed)
            btnThursday = itemView.findViewById(R.id.thur)
            btnFriday = itemView.findViewById(R.id.fri)
            btnSaturday = itemView.findViewById(R.id.sat)
            btnSunday = itemView.findViewById(R.id.sun)
            selectSong = itemView.findViewById(R.id.tv_chosen_song)
            delete = itemView.findViewById(R.id.delete_alarm_item)
            vibrate = itemView.findViewById(R.id.vibrate_checkbox)
            extraContent = itemView.findViewById(R.id.expanded_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.alarm_card_item, parent, false)

        return AlarmItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmItemViewHolder, position: Int) {
        val isExpanded: Boolean = position == mExpandedPosition

        if (data[position].label == null) {
            holder.addLabel.text = context.getText(R.string.add_label)
        } else {
            holder.addLabel.text = data[position].label
        }

        if (data[position].schedule.isEmpty()) {
            holder.alarmSchedule.text = context.getText(R.string.not_scheduled)
        } else {
            if (data[position].schedule.size == 1) {
                holder.alarmSchedule.text = data[position].schedule[0]
            } else {
                for (day in data[position].schedule) {
                    holder.alarmSchedule.text = "${day.slice(0..2)}, "
                }
            }
        }

        holder.currentTime.text = data[position].time

        holder.addLabel.setOnClickListener {
            AddLabelDialog.newInstance(position)
                .show((context as AppCompatActivity).supportFragmentManager, "Add Label")
        }

        holder.activateAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // TODO: Activate alarm functionality
            }
        }

        holder.expandOrCollapseItem.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            TransitionManager.beginDelayedTransition(mRecyclerView)
            notifyItemChanged(position)
        }

        holder.extraContent.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.currentTime.setOnClickListener {
            val picker = TimePicker.buildPicker("Set Alarm")
            picker.show((context as AppCompatActivity).supportFragmentManager, "Reschedule Alarm")
            picker.addOnPositiveButtonClickListener {
                data[position].time = "${picker.hour}:${picker.minute}"
                notifyItemChanged(position)
            }
        }

        holder.delete.setOnClickListener {
            data.removeAt(position)
            notifyItemRemoved(position)
        }

        holder.vibrate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                VibrateSingleton.vibrateDevice(context, true)
            }
        }

        holder.btnMonday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data[position].schedule.add("Monday")
            } else {
                data[position].schedule.remove("Monday")
            }
        }

        holder.btnTuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data[position].schedule.add("Tuesday")
            } else {
                data[position].schedule.remove("Tuesday")
            }
        }

        holder.btnWednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data[position].schedule.add("Wednesday")
            } else {
                data[position].schedule.remove("Wednesday")
            }
        }

        holder.btnThursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data[position].schedule.add("Thursday")
            } else {
                data[position].schedule.remove("Thursday")
            }
        }

        holder.btnFriday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data[position].schedule.add("Friday")
            } else {
                data[position].schedule.remove("Friday")
            }
        }

        holder.btnSaturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data[position].schedule.add("Saturday")
            } else {
                data[position].schedule.remove("Saturday")
            }
        }

        holder.btnSunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data[position].schedule.add("Sunday")
            } else {
                data[position].schedule.remove("Sunday")
            }
        }

        holder.selectSong.setOnClickListener {
            val intent = Intent(context, SoundActivity::class.java)
            intent.putExtra(SOUND_SCREEN_TITLE, "Alarm Sound")
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAlarmList(newList: ArrayList<AlarmItemModel>) {
        data.clear()
        data = newList
        notifyDataSetChanged()
    }
}