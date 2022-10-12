package com.github.adamr22.stopwatch

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.adamr22.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest

class StopWatchFragment : Fragment() {

    private val TAG = "StopWatchFragment"

    companion object {
        fun newInstance() = StopWatchFragment()
    }

    private lateinit var btnReset: FloatingActionButton
    private lateinit var btnPause: ImageButton
    private lateinit var btnPlay: FloatingActionButton
    private lateinit var btnAddLap: FloatingActionButton

    private lateinit var stopwatchTime: TextView

    private lateinit var viewModel: StopWatchViewModel
    private lateinit var lapTimeLists: ListView


    private val timeStamps = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[StopWatchViewModel::class.java]
        viewModel.changeState(viewModel.initialOrdinalValue)

        Log.d(TAG, "onCreate: OnCreate Called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireContext() as AppCompatActivity).supportActionBar?.title = "Stopwatch"

        return inflater.inflate(R.layout.fragment_stop_watch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnReset = view.findViewById(R.id.btn_reset_stopwatch)
        btnAddLap = view.findViewById(R.id.btn_add_lap)
        btnPlay = view.findViewById(R.id.play)
        btnPause = view.findViewById(R.id.btn_pause)

        stopwatchTime = view.findViewById(R.id.stopwatch_content)

        lapTimeLists = view.findViewById(R.id.lap_list)

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.lap_item_layout,
            R.id.tv_lap_time,
            timeStamps
        )

        lapTimeLists.adapter = adapter

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {

        lapTimeLists.visibility = if (timeStamps.isEmpty()) View.GONE else View.VISIBLE

        btnPause.setOnClickListener {
            viewModel.changeState(viewModel.pauseOrdinalValue)
        }

        btnPlay.setOnClickListener {
            viewModel.changeState(viewModel.playOrdinalValue)
        }

        renderUI()

        super.onResume()
    }

    private fun renderUI() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.stopwatchState.collectLatest {
                when (it) {
                    StopWatchViewModel.StopWatchStates.INITIAL -> {
                        btnPause.visibility = View.GONE
                        btnAddLap.visibility = View.GONE
                        btnReset.visibility = View.GONE
                        btnPlay.visibility = View.VISIBLE
                    }
                    StopWatchViewModel.StopWatchStates.PAUSED -> {
                        btnPause.visibility = View.GONE
                        btnAddLap.visibility = View.GONE
                        btnReset.visibility = View.VISIBLE
                        btnPlay.visibility = View.VISIBLE
                    }
                    StopWatchViewModel.StopWatchStates.RUNNING -> {
                        btnPause.visibility = View.VISIBLE
                        btnAddLap.visibility = View.VISIBLE
                        btnReset.visibility = View.VISIBLE
                        btnPlay.visibility = View.GONE
                    }
                }
            }
        }
    }
}