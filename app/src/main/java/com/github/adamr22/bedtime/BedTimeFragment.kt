package com.github.adamr22.bedtime

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.adamr22.R

class BedTimeFragment : Fragment() {

    companion object {
        fun newInstance() = BedTimeFragment()
    }

    private lateinit var viewModel: BedTimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[BedTimeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.bedtime)

        return inflater.inflate(R.layout.fragment_bed_time, container, false)
    }
}