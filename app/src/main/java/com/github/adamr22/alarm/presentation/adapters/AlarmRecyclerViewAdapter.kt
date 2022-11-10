package com.github.adamr22.alarm.presentation.adapters

import android.content.Context
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.AlarmAndDay
import com.github.adamr22.utils.AddLabelDialog
import com.github.adamr22.utils.PickAlarmInterface
import com.github.adamr22.utils.TimePicker
import com.github.adamr22.utils.VibrateSingleton
import com.google.android.material.switchmaterial.SwitchMaterial

class AlarmRecyclerViewAdapter(
    private val context: Context,
    private val viewModel: AlarmViewModel
) :
    RecyclerView.Adapter<AlarmRecyclerViewAdapter.AlarmItemViewHolder>() {

    private var mExpandedPosition: Int = -1
    private lateinit var mRecyclerView: RecyclerView

    private val pickAlarmInterface by lazy {
        context as PickAlarmInterface
    }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<AlarmAndDay>() {
        override fun areItemsTheSame(oldItem: AlarmAndDay, newItem: AlarmAndDay): Boolean {
            return oldItem.alarm.id == newItem.alarm.id
        }

        override fun areContentsTheSame(oldItem: AlarmAndDay, newItem: AlarmAndDay): Boolean {
            return oldItem == newItem
        }
    }

    val data = AsyncListDiffer(this, diffUtilCallback)

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
        val dataItem = data.currentList[position]

        val isExpanded: Boolean = position == mExpandedPosition

        holder.activateAlarm.isChecked = dataItem.alarm.isScheduled
        holder.vibrate.isChecked = dataItem.alarm.vibrates

        if (dataItem.alarm.label == null) {
            holder.addLabel.text = context.getText(R.string.add_label)
        } else {
            holder.addLabel.text = dataItem.alarm.label
        }

        if (dataItem.dayOfWeek.isNotEmpty()) {
            if (holder.activateAlarm.isChecked) {
                if (dataItem.dayOfWeek.size == 1) {
                    holder.alarmSchedule.text = dataItem.dayOfWeek[0].day
                } else {
                    holder.alarmSchedule.text = dataItem.dayOfWeek.map {
                        it.day?.slice(0..2)
                    }.toString().replace("[", "").replace("]", "")
                }
            }

            if (!holder.activateAlarm.isChecked) holder.alarmSchedule.text = context.getText(R.string.not_scheduled)
        }

        if (dataItem.dayOfWeek.isEmpty()) {
            if (holder.activateAlarm.isChecked) context.getText(R.string.scheduled)

            if (!holder.activateAlarm.isChecked) context.getText(R.string.not_scheduled)
        }

        holder.currentTime.text = String.format(
            context.resources.getString(R.string.default_time_2),
            "%02d".format(dataItem.alarm.hour),
            "%02d".format(dataItem.alarm.minute)
        )

        holder.addLabel.setOnClickListener {
            AddLabelDialog.newInstance(position, viewModel, null, dataItem.alarm.id)
                .show((context as AppCompatActivity).supportFragmentManager, "Add Label")
        }

        holder.activateAlarm.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                viewModel.updateAlarm(
                    Alarm(
                        dataItem.alarm.id,
                        dataItem.alarm.label,
                        dataItem.alarm.title,
                        dataItem.alarm.uri,
                        true,
                        dataItem.alarm.sunriseMode,
                        dataItem.alarm.vibrates,
                        dataItem.alarm.hour,
                        dataItem.alarm.minute
                    )
                )
            } else {
                viewModel.updateAlarm(
                    Alarm(
                        dataItem.alarm.id,
                        dataItem.alarm.label,
                        dataItem.alarm.title,
                        dataItem.alarm.uri,
                        false,
                        dataItem.alarm.sunriseMode,
                        dataItem.alarm.vibrates,
                        dataItem.alarm.hour,
                        dataItem.alarm.minute
                    )
                )
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
                viewModel.updateAlarm(
                    Alarm(
                        dataItem.alarm.id,
                        dataItem.alarm.label,
                        dataItem.alarm.title,
                        dataItem.alarm.uri,
                        dataItem.alarm.isScheduled,
                        dataItem.alarm.sunriseMode,
                        dataItem.alarm.vibrates,
                        picker.hour,
                        picker.minute
                    )
                )
            }
        }

        holder.delete.setOnClickListener {
            dataItem.dayOfWeek.forEach { dayOfWeek ->
                viewModel.deleteSchedule(dayOfWeek)
            }
            viewModel.deleteAlarm(dataItem.alarm)
        }

        holder.vibrate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                VibrateSingleton.vibrateDeviceOnce(context, true)
                viewModel.updateAlarm(
                    Alarm(
                        dataItem.alarm.id,
                        dataItem.alarm.label,
                        dataItem.alarm.title,
                        dataItem.alarm.uri,
                        dataItem.alarm.isScheduled,
                        dataItem.alarm.sunriseMode,
                        true,
                        dataItem.alarm.hour,
                        dataItem.alarm.minute
                    )
                )
            } else {
                viewModel.updateAlarm(
                    Alarm(
                        dataItem.alarm.id,
                        dataItem.alarm.label,
                        dataItem.alarm.title,
                        dataItem.alarm.uri,
                        dataItem.alarm.isScheduled,
                        dataItem.alarm.sunriseMode,
                        false,
                        dataItem.alarm.hour,
                        dataItem.alarm.minute
                    )
                )
            }
        }

        holder.btnMonday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.insertSchedule(context.getString(R.string.monday), dataItem.alarm.id!!)
            } else {
                viewModel.deleteDayFromSchedule(context.getString(R.string.monday), dataItem.alarm.id!!)
            }
        }

        holder.btnTuesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.insertSchedule(context.getString(R.string.tuesday), dataItem.alarm.id!!)
            } else {
                viewModel.deleteDayFromSchedule(context.getString(R.string.tuesday), dataItem.alarm.id!!)
            }
        }

        holder.btnWednesday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.insertSchedule(context.getString(R.string.wednesday), dataItem.alarm.id!!)
            } else {
                viewModel.deleteDayFromSchedule(context.getString(R.string.wednesday), dataItem.alarm.id!!)
            }
        }

        holder.btnThursday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.insertSchedule(context.getString(R.string.thursday), dataItem.alarm.id!!)
            } else {
                viewModel.deleteDayFromSchedule(context.getString(R.string.thursday), dataItem.alarm.id!!)
            }
        }

        holder.btnFriday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.insertSchedule(context.getString(R.string.friday), dataItem.alarm.id!!)
            } else {
                viewModel.deleteDayFromSchedule(context.getString(R.string.friday), dataItem.alarm.id!!)
            }
        }

        holder.btnSaturday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.insertSchedule(context.getString(R.string.saturday), dataItem.alarm.id!!)
            } else {
                viewModel.deleteDayFromSchedule(context.getString(R.string.saturday), dataItem.alarm.id!!)
            }
        }

        holder.btnSunday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.insertSchedule(context.getString(R.string.sunday), dataItem.alarm.id!!)
            } else {
                viewModel.deleteDayFromSchedule(context.getString(R.string.sunday), dataItem.alarm.id!!)
            }
        }

        holder.selectSong.text = dataItem.alarm.title

        holder.selectSong.setOnClickListener {
            pickAlarmInterface.selectAlarmTone()
        }
    }

    override fun getItemCount(): Int {
        return data.currentList.size
    }

}