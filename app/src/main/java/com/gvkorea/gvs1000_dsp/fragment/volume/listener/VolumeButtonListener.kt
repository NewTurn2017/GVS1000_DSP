package com.gvkorea.gvs1000_dsp.fragment.volume.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.volume.presenter.VolumePresenter

class VolumeButtonListener(val presenter: VolumePresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_volumeReset -> presenter.volumeReset()
            R.id.btn_allMute -> presenter.muteAll()
            R.id.btn_saveVolume -> presenter.savePreset()
            R.id.btn_loadVolume -> presenter.loadPreset()
        }
    }
}