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

    private val diffUtilCallback = object : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
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

        holder.activateAlarm.isChecked = dataItem.isScheduled
        holder.vibrate.isChecked = dataItem.vibrates

        if (dataItem.label == null) {
            holder.addLabel.text = context.getText(R.string.add_label)
        } else {
            holder.addLabel.text = dataItem.label
        }

        if (dataItem.everyDay) {
            if (holder.activateAlarm.isChecked) {
                context.getText(R.string.scheduled_daily)
            }

            if (!holder.activateAlarm.isChecked) holder.alarmSchedule.text =
                context.getText(R.string.not_scheduled)
        }

        if (!dataItem.everyDay) {
            if (holder.activateAlarm.isChecked) context.getText(R.string.scheduled)

            if (!holder.activateAlarm.isChecked) context.getText(R.string.not_scheduled)
        }

        holder.currentTime.text = String.format(
            context.resources.getString(R.string.default_time_2),
            "%02d".format(dataItem.hour),
            "%02d".format(dataItem.minute)
        )

        holder.addLabel.setOnClickListener {
            AddLabelDialog.newInstance(position, viewModel, null, dataItem.id)
                .show((context as AppCompatActivity).supportFragmentManager, "Add Label")
        }

        holder.activateAlarm.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                viewModel.updateAlarm(
                    Alarm(
                        dataItem.id,
                        dataItem.label,
                        dataItem.title,
                        dataItem.uri,
                        true,
                        dataItem.sunriseMode,
                        dataItem.vibrates,
                        dataItem.expandedItem,
                        dataItem.everyDay,
                        dataItem.hour,
                        dataItem.minute
                    )
                )
            } else {
                viewModel.updateAlarm(
                    Alarm(
                        dataItem.id,
                        dataItem.label,
                        dataItem.title,
                        dataItem.uri,
                        false,
                        dataItem.sunriseMode,
                        dataItem.vibrates,
                        dataItem.expandedItem,
                        dataItem.everyDay,
                        dataItem.hour,
                        dataItem.minute
                    )
                )
            }
        }

        holder.expandOrCollapseItem.setOnClickListener {
            toggleLayout(
                dataItem.expandedItem,
                extraContent = holder.extraContent,
                holder.expandOrCollapseItem,
                dataItem.id!!
            )
        }

        holder.currentTime.setOnClickListener {
            val picker = TimePicker.buildPicker("Set Alarm")
            picker.show((context as AppCompatActivity).supportFragmentManager, "Reschedule Alarm")
            picker.addOnPositiveButtonClickListener {
                viewModel.updateAlarm(
                    Alarm(
                        dataItem.id,
                        dataItem.label,
                        dataItem.title,
                        dataItem.uri,
                        dataItem.isScheduled,
                        dataItem.sunriseMode,
                        dataItem.vibrates,
                        dataItem.expandedItem,
                        dataItem.everyDay,
                        picker.hour,
                        picker.minute
                    )
                )
            }
        }

        holder.delete.setOnClickListener {
            viewModel.deleteAlarm(dataItem)
        }

        holder.vibrate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                VibrateSingleton.vibrateDeviceOnce(context, true)
                viewModel.updateAlarm(
                    Alarm(
                        dataItem.id,
                        dataItem.label,
                        dataItem.title,
                        dataItem.uri,
                        dataItem.isScheduled,
                        dataItem.sunriseMode,
                        true,
                        dataItem.expandedItem,
                        dataItem.everyDay,
                        dataItem.hour,
                        dataItem.minute
                    )
                )
            } else {
                viewModel.updateAlarm(
                    Alarm(
                        dataItem.id,
                        dataItem.label,
                        dataItem.title,
                        dataItem.uri,
                        dataItem.isScheduled,
                        dataItem.sunriseMode,
                        false,
                        dataItem.expandedItem,
                        dataItem.everyDay,
                        dataItem.hour,
                        dataItem.minute
                    )
                )
            }
        }

        holder.dailyAlarm.isChecked = dataItem.everyDay

        holder.dailyAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.updateDaily(true, dataItem.id!!)

            if (!isChecked) viewModel.updateDaily(false, dataItem.id!!)
        }

        holder.selectSong.text = dataItem.title

        holder.selectSong.setOnClickListener {
            pickAlarmInterface.selectAlarmTone()
        }

        pickAlarmInterface.returnSelectedTone()?.let {
            val title = pickAlarmInterface.returnSelectedTone()!!.second
            val uri = pickAlarmInterface.returnSelectedTone()!!.first

            viewModel.updateAlarm(
                Alarm(
                    dataItem.id,
                    dataItem.label,
                    title,
                    uri,
                    dataItem.isScheduled,
                    dataItem.sunriseMode,
                    dataItem.vibrates,
                    dataItem.expandedItem,
                    dataItem.everyDay,
                    dataItem.hour,
                    dataItem.minute,
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
        view: ImageButton,
        id: Int
    ) {
        if (expanded) {
            Animations.collapse(extraContent)
            view.setImageResource(R.drawable.ic_arrow_up)
            viewModel.updateExpandedItem(false, id)
        } else {
            Animations.expand(extraContent)
            view.setImageResource(R.drawable.ic_arrow_down)
            viewModel.updateExpandedItem(true, id)
        }

    }
}