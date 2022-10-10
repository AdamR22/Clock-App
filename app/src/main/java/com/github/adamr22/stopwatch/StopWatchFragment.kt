package com.github.adamr22.stopwatch

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.adamr22.R

class StopWatchFragment : Fragment() {

    companion object {
        fun newInstance() = StopWatchFragment()
    }

    private lateinit var viewModel: StopWatchViewModel
    private val exampleList = arrayOf("Hey", "Hello", "Niaje", "Rada", "Irio nade", "Ilal Mang'eny")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StopWatchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireContext() as AppCompatActivity).supportActionBar?.title = "Stopwatch"

        return inflater.inflate(R.layout.fragment_stop_watch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listView: ListView = view.findViewById(R.id.lap_list)

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.lap_item_layout,
            R.id.tv_lap_time,
            exampleList
        )

        listView.adapter = adapter

        super.onViewCreated(view, savedInstanceState)
    }

}