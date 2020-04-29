package com.gvkorea.gvs1000_dsp.fragment.volume.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.volume.VolumeFragment
import com.gvkorea.gvs1000_dsp.fragment.volume.presenter.VolumePresenter
import kotlinx.android.synthetic.main.fragment_volume.*

class MuteButtonListener(val view: VolumeFragment, val presenter: VolumePresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_spk1_mute -> presenter.mute(0 ,spkList[0], view.btn_spk1_mute)
            R.id.btn_spk2_mute -> presenter.mute(1 ,spkList[1], view.btn_spk2_mute)
            R.id.btn_spk3_mute -> presenter.mute(2 ,spkList[2], view.btn_spk3_mute)
            R.id.btn_spk4_mute -> presenter.mute(3 ,spkList[3], view.btn_spk4_mute)
            R.id.btn_spk5_mute -> presenter.mute(4 ,spkList[4], view.btn_spk5_mute)
            R.id.btn_spk6_mute -> presenter.mute(5 ,spkList[5], view.btn_spk6_mute)
            R.id.btn_spk7_mute -> presenter.mute(6 ,spkList[6], view.btn_spk7_mute)
            R.id.btn_spk8_mute -> presenter.mute(7 ,spkList[7], view.btn_spk8_mute)
        }
    }
}