package com.gvkorea.gvs1000_dsp.fragment.volume.listener

import android.widget.SeekBar
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.volume.VolumeFragment
import com.gvkorea.gvs1000_dsp.fragment.volume.presenter.VolumePresenter
import kotlinx.android.synthetic.main.fragment_volume.*

class VolumeSeekBarChangeListener(val view: VolumeFragment, val presenter: VolumePresenter) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            when(seekBar?.id){
                R.id.sb_spk1 -> presenter.volumeControl(spkList[0], progress, view.tv_spk1)
                R.id.sb_spk2 -> presenter.volumeControl(spkList[1], progress, view.tv_spk2)
                R.id.sb_spk3 -> presenter.volumeControl(spkList[2], progress, view.tv_spk3)
                R.id.sb_spk4 -> presenter.volumeControl(spkList[3], progress, view.tv_spk4)
                R.id.sb_spk5 -> presenter.volumeControl(spkList[4], progress, view.tv_spk5)
                R.id.sb_spk6 -> presenter.volumeControl(spkList[5], progress, view.tv_spk6)
                R.id.sb_spk7 -> presenter.volumeControl(spkList[6], progress, view.tv_spk7)
                R.id.sb_spk8 -> presenter.volumeControl(spkList[7], progress, view.tv_spk8)

        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
//        when(seekBar?.id){
//            R.id.sb_spk1_wb -> view.sb_spk1_wb.setBackgroundResource(R.color.seekbar_wrapper_change)
//            R.id.sb_spk2_wb -> view.sb_spk2_wb.setBackgroundResource(R.color.seekbar_wrapper_change)
//            R.id.sb_spk3_wb -> view.sb_spk3_wb.setBackgroundResource(R.color.seekbar_wrapper_change)
//            R.id.sb_spk4_wb -> view.sb_spk4_wb.setBackgroundResource(R.color.seekbar_wrapper_change)
//            R.id.sb_spk5_wb -> view.sb_spk5_wb.setBackgroundResource(R.color.seekbar_wrapper_change)
//            R.id.sb_spk6_wb -> view.sb_spk6_wb.setBackgroundResource(R.color.seekbar_wrapper_change)
//            R.id.sb_spk7_wb -> view.sb_spk7_wb.setBackgroundResource(R.color.seekbar_wrapper_change)
//            R.id.sb_spk8_wb -> view.sb_spk8_wb.setBackgroundResource(R.color.seekbar_wrapper_change)
//        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
//        when(seekBar?.id){
//            R.id.sb_spk1_wb -> view.sb_spk1_wb.background = null
//            R.id.sb_spk2_wb -> view.sb_spk2_wb.background = null
//            R.id.sb_spk3_wb -> view.sb_spk3_wb.background = null
//            R.id.sb_spk4_wb -> view.sb_spk4_wb.background = null
//            R.id.sb_spk5_wb -> view.sb_spk5_wb.background = null
//            R.id.sb_spk6_wb -> view.sb_spk6_wb.background = null
//            R.id.sb_spk7_wb -> view.sb_spk7_wb.background = null
//            R.id.sb_spk8_wb -> view.sb_spk8_wb.background = null
//        }
    }
}