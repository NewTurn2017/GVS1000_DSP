package com.gvkorea.gvs1000_dsp.fragment.volume.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.MainActivity
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.volume.VolumeFragment
import com.gvkorea.gvs1000_dsp.fragment.volume.presenter.VolumePresenter
import kotlinx.android.synthetic.main.fragment_volume.*

class VolumeUpButtonListener(val view: VolumeFragment, val presenter: VolumePresenter): View.OnClickListener {
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_spk1_up ->  presenter.volumeUpControl(MainActivity.spkList[0], view.sb_spk1, view.tv_spk1)
            R.id.btn_spk2_up ->  presenter.volumeUpControl(MainActivity.spkList[1], view.sb_spk2, view.tv_spk2)
            R.id.btn_spk3_up ->  presenter.volumeUpControl(MainActivity.spkList[2], view.sb_spk3, view.tv_spk3)
            R.id.btn_spk4_up ->  presenter.volumeUpControl(MainActivity.spkList[3], view.sb_spk4, view.tv_spk4)
            R.id.btn_spk5_up ->  presenter.volumeUpControl(MainActivity.spkList[4], view.sb_spk5, view.tv_spk5)
            R.id.btn_spk6_up ->  presenter.volumeUpControl(MainActivity.spkList[5], view.sb_spk6, view.tv_spk6)
            R.id.btn_spk7_up ->  presenter.volumeUpControl(MainActivity.spkList[6], view.sb_spk7, view.tv_spk7)
            R.id.btn_spk8_up ->  presenter.volumeUpControl(MainActivity.spkList[7], view.sb_spk8, view.tv_spk8)
        }

    }
}