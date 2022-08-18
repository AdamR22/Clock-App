package com.github.adamr22.alarm.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.alarm.data.models.AlarmItemModel
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import com.github.adamr22.common.AddLabelDialog
import com.github.adamr22.common.TimePicker
import com.github.adamr22.common.VibrateSingleton
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.flow.collectLatest

class AlarmRecyclerViewAdapter(
    private val context: Context,
    private val viewModel: AlarmViewModel
) :
    RecyclerView.Adapter<AlarmRecyclerViewAdapter.AlarmItemViewHolder>() {

    private var data: List<AlarmItemModel> = mutableListOf()

    private var mExpandedPosition: Int = -1
    private lateinit var mRecyclerView: RecyclerView
    private val VIEW_MODEL = "Alarm View Model"
    private val ALARM_ITEM_INDEX = "Alarm Item Index"
    private val ALARM_TONE_TITLE = "Alarm Tone Title"

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

        if (data[position].label == "") {
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
                holder.alarmSchedule.text = data[position].schedule.map { s: String ->
                    s.slice(0..2)
                }.toString().replace("[", "").replace("]", "")
            }
        }

        holder.currentTime.text = data[position].time

        holder.addLabel.setOnClickListener {
            AddLabelDialog.newInstance(position, viewModel)
                .show((context as AppCompatActivity).supportFragmentManager, "Add Label")


            context.lifecycleScope.launchWhenCreated {
                viewModel.labelChanged.collectLatest {
                    when (it) {
                        is AlarmViewModel.LabelChangedState.Changed -> notifyItemChanged(position)
                        else -> {}
                    }
                }
            }

        }

        holder.activateAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            }
        }

        holder.expandOrCollapseItem.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            TransitionManager.beginDelayedTransition(mRecyclerView)
            notifyItemChanged(position)
        }

        if (isExpanded) {
            holder.expandOrCollapseItem.setImageResource(R.drawable.ic_arrow_up)
        } else {
            holder.expandOrCollapseItem.setImageResource(R.drawable.ic_arrow_down)
        }

        holder.extraContent.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.currentTime.setOnClickListener {
            val picker = TimePicker.buildPicker("Set Alarm")
            picker.show((context as AppCompatActivity).supportFragmentManager, "Reschedule Alarm")
            picker.addOnPositiveButtonClickListener {
                viewModel.changeTime(position, "%02d:%02d".format(picker.hour, picker.minute))
                notifyItemChanged(position)
            }
        }

        holder.delete.setOnClickListener {
            viewModel.deleteItem(position)
            notifyItemRemoved(position)
        }

        holder.vibrate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                VibrateSingleton.vibrateDeviceOnce(context, true)
            } else {
                VibrateSingleton.vibrateDeviceOnce(context, false)
            }
        }

        holder.btnMonday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addDayToSchedule(position, "Monday")
                notifyItemChanged(position)
            } else {
                viewModel.removeDayOnSchedule(position, "Monday")
                notifyItemChanged(position)
            }
        }

        holder.btnTuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addDayToSchedule(position, "Tuesday")
                notifyItemChanged(position)
            } else {
                viewModel.removeDayOnSchedule(position, "Tuesday")
                notifyItemChanged(position)
            }
        }

        holder.btnWednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addDayToSchedule(position, "Wednesday")
                notifyItemChanged(position)
            } else {
                viewModel.removeDayOnSchedule(position, "Wednesday")
                notifyItemChanged(position)
            }
        }

        holder.btnThursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addDayToSchedule(position, "Thursday")
                notifyItemChanged(position)
            } else {
                viewModel.removeDayOnSchedule(position, "Thursday")
                notifyItemChanged(position)
            }
        }

        holder.btnFriday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addDayToSchedule(position, "Friday")
                notifyItemChanged(position)
            } else {
                viewModel.removeDayOnSchedule(position, "Friday")
                notifyItemChanged(position)
            }
        }

        holder.btnSaturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addDayToSchedule(position, "Saturday")
                notifyItemChanged(position)
            } else {
                viewModel.removeDayOnSchedule(position, "Saturday")
                notifyItemChanged(position)
            }
        }

        holder.btnSunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addDayToSchedule(position, "Saturday")
                notifyItemChanged(position)
            } else {
                viewModel.removeDayOnSchedule(position, "Sunday")
                notifyItemChanged(position)
            }
        }

        holder.selectSong.text.apply {
            data[position].ringtoneTitle
            notifyItemChanged(position)
        }

        holder.selectSong.setOnClickListener {
            val ringtoneTitle = data[position].ringtoneTitle
            Intent(RingtoneManager.ACTION_RINGTONE_PICKER).run {
                putExtra(VIEW_MODEL, viewModel)
                putExtra(ALARM_ITEM_INDEX, position)
                putExtra(ALARM_TONE_TITLE, ringtoneTitle)
                putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Alarm Sound")
                putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
                putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
                putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                context.startActivity(this)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAlarmList(newList: List<AlarmItemModel>) {
        data = mutableListOf()
        newList.forEach {
            (data as MutableList<AlarmItemModel>).add(it)
        }
        notifyDataSetChanged()
    }
}