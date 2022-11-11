package com.github.adamr22.alarm.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.entities.AlarmAndDay
import com.github.adamr22.utils.*
import com.google.android.material.switchmaterial.SwitchMaterial

class AlarmRecyclerViewAdapter(
    private val context: Context,
    private val viewModel: AlarmViewModel
) :
    RecyclerView.Adapter<AlarmRecyclerViewAdapter.AlarmItemViewHolder>() {

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
        val selectSong: TextView
        val delete: TextView
        val vibrate: CheckBox
        val extraContent: LinearLayout
        val dailyAlarm: CheckBox

        init {
            addLabel = itemView.findViewById(R.id.tv_label)
            expandOrCollapseItem = itemView.findViewById(R.id.expand_or_collapse_alarm_item)
            currentTime = itemView.findViewById(R.id.set_time)
            alarmSchedule = itemView.findViewById(R.id.tv_days_scheduled)
            activateAlarm = itemView.findViewById(R.id.switch_alarm_on_off)
            selectSong = itemView.findViewById(R.id.tv_chosen_song)
            delete = itemView.findViewById(R.id.delete_alarm_item)
            vibrate = itemView.findViewById(R.id.vibrate_checkbox)
            extraContent = itemView.findViewById(R.id.expanded_content)
            dailyAlarm = itemView.findViewById(R.id.daily_alarm_checkbox)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.alarm_card_item, parent, false)

        return AlarmItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmItemViewHolder, position: Int) {
        val dataItem = data.currentList[position]
        var isExpanded = false

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

            if (!holder.activateAlarm.isChecked) holder.alarmSchedule.text =
                context.getText(R.string.not_scheduled)
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
            isExpanded = toggleLayout(
                isExpanded,
                extraContent = holder.extraContent,
                holder.expandOrCollapseItem
            )
        }

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

        //TODO: Add single checkbox for everyday alarm or single instance alarm

        holder.selectSong.text = dataItem.alarm.title

        holder.selectSong.setOnClickListener {
            pickAlarmInterface.selectAlarmTone()
        }

        pickAlarmInterface.returnSelectedTone()?.let {
            val title = pickAlarmInterface.returnSelectedTone()!!.second
            val uri = pickAlarmInterface.returnSelectedTone()!!.first

            viewModel.updateAlarm(
                Alarm(
                    dataItem.alarm.id,
                    dataItem.alarm.label,
                    title,
                    uri,
                    dataItem.alarm.isScheduled,
                    dataItem.alarm.sunriseMode,
                    dataItem.alarm.vibrates,
                    dataItem.alarm.hour,
                    dataItem.alarm.minute,
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return data.currentList.size
    }

    private fun toggleLayout(
        expanded: Boolean,
        extraContent: LinearLayout,
        view: ImageButton
    ): Boolean {
        if (expanded) {
            Animations.collapse(extraContent)
            view.setImageResource(R.drawable.ic_arrow_up)
            return false
        }

        Animations.expand(extraContent)
        view.setImageResource(R.drawable.ic_arrow_down)
        return true
    }
}