package com.github.adamr22.timer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.common.AddLabelDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import java.util.*

class RunTimerAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val viewModel: TimerViewModel
) :
    RecyclerView.Adapter<RunTimerAdapter.RunTimerViewHolder>() {

    private var listOfTimers = mutableListOf<TimerModel>()

    inner class RunTimerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val addLabel: TextView
        val tvSetTime: TextView
        val tvAddOneMinOrReset: TextView

        val btnPlayTimer: FloatingActionButton
        val btnDeleteTimer: FloatingActionButton
        val btnAddTimer: FloatingActionButton
        val btnPauseTimer: ImageButton
        val pbTimer: ProgressBar

        init {
            btnPlayTimer = itemView.findViewById(R.id.play)
            btnDeleteTimer = itemView.findViewById(R.id.btn_delete_timer)
            btnAddTimer = itemView.findViewById(R.id.btn_add_timer)
            btnPauseTimer = itemView.findViewById(R.id.btn_pause)
            pbTimer = itemView.findViewById(R.id.pb_timer)

            addLabel = itemView.findViewById(R.id.tv_timer_label)
            tvSetTime = itemView.findViewById(R.id.tv_set_timer)
            tvAddOneMinOrReset = itemView.findViewById(R.id.tv_add_one_minute)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunTimerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.run_timer_layout, parent, false)
        return RunTimerViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RunTimerViewHolder, position: Int) {
        val timeInstance = listOfTimers[position].setTime

        val hours = timeInstance.get(Calendar.HOUR_OF_DAY)
        val minutes = timeInstance.get(Calendar.MINUTE)
        val seconds = timeInstance.get(Calendar.SECOND)

        val timeSetTextView = holder.tvSetTime

        if (hours == 0 && minutes == 0 && seconds != 0)
            timeSetTextView.text = "$seconds"

        if (hours == 0 && minutes != 0)
            timeSetTextView.text = "$minutes:$seconds"

        if (hours != 0)
            timeSetTextView.text = "$hours:$minutes:$seconds"

        holder.addLabel.text = listOfTimers[position].label ?: ""

        holder.addLabel.setOnClickListener {
            AddLabelDialog.newInstance(position, null, viewModel)
        }

        holder.btnDeleteTimer.setOnClickListener {
            viewModel.deleteTimer(position)

            (context as AppCompatActivity).lifecycleScope.launchWhenCreated {
                viewModel.timers.collectLatest {
                    when (it) {
                        is TimerViewModel.TimerFragmentUIState.Timers -> {
                            if (it.timerInstances.isEmpty()) {
                                viewModel.currentFragment = 0
                                fragmentManager.popBackStack()
                            }
                        }

                        else -> {}
                    }
                }
            }

            notifyItemChanged(position)
        }

        holder.btnAddTimer.setOnClickListener {
            viewModel.currentFragment = 0
            fragmentManager.popBackStack()
        }

        holder.btnPauseTimer.setOnClickListener {
            // TODO: Function to stop timer
        }
    }

    override fun getItemCount(): Int {
        return listOfTimers.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<TimerModel>) {
        listOfTimers.clear()
        newList.forEach {
            listOfTimers.add(it)
        }
        notifyDataSetChanged()
    }
}