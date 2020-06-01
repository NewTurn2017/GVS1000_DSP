package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.listener

import android.widget.CompoundButton
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.EvalFragment.Companion.isEvalRepeat

class EvalcheckChangeListener : CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        isEvalRepeat = buttonView?.isChecked!!
    }
}