package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.listener

import android.view.View
import android.widget.AdapterView
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.EvalFragment
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.EvalFragment.Companion.averageTime

class SpinnerItemSeletedListener(val fragment: EvalFragment) : AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val time = fragment.resources.getStringArray(R.array.avgTime_array)[position]
        averageTime = time.toInt() * 1000

    }
}