package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.listener

import android.widget.CompoundButton
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment.Companion.isRepeat

class CheckChangeListener : CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        isRepeat = buttonView?.isChecked!!
    }
}