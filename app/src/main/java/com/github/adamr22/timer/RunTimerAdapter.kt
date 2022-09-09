package com.github.adamr22.timer

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception

class RunTimerAdapter(private val listOfTimers: List<String>, private val context: Context) :
    RecyclerView.Adapter<RunTimerAdapter.RunTimerViewHolder>() {

    private val TAG = "RunTimerAdapter"
    private var progress = 0

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

    override fun onBindViewHolder(holder: RunTimerViewHolder, position: Int) {
        holder.tvSetTime.text = listOfTimers[position]
        holder.btnPauseTimer.setOnClickListener {
            Log.d(TAG, "onBindViewHolder: btn pause pressed")
            Thread {
                while (progress < 100) {
                    progress += 1
                    Handler(Looper.getMainLooper()).post {
                        holder.pbTimer.progress = progress
                    }

                    try {
                        Thread.sleep(200)
                    } catch (e: Exception) {
                        Log.d(TAG, "onBindViewHolder: ${e.printStackTrace()}")
                    }
                }
            }.start()
        }
    }

    override fun getItemCount(): Int {
        return listOfTimers.size
    }
}