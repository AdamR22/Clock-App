package com.github.adamr22.clockapp.timer.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.adamr22.clockapp.R

class SetTimerAdapter(private val numpadText: Array<String>, private val context: Context) :
    BaseAdapter() {

    override fun getCount(): Int {
        return numpadText.size
    }

    override fun getItem(p0: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val numpadText = numpadText[position]
        var mConvertView = convertView

        if (convertView == null) {
            mConvertView = LayoutInflater.from(context).inflate(R.layout.numpad_button, null)
        }

        val textView = mConvertView?.findViewById(R.id.numpad_item) as TextView

        textView.also { tv ->
            tv.text = numpadText
            tv.background = if (numpadText == "X") ContextCompat.getDrawable(
                context,
                R.drawable.timer_button_grey
            ) else ContextCompat.getDrawable(context, R.drawable.timer_button_black)
        }

        return mConvertView
    }
}