package com.github.adamr22.bedtime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.adamr22.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BedTImeFragmentBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "BedTime Modal Bottom Sheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_bedtime_fragment_bottom_sheet, container, false)
    }

}