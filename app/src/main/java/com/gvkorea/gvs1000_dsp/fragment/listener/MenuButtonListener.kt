package com.gvkorea.gvs1000_dsp.fragment.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.presenter.MainPresenter
import com.gvkorea.gvs1000_dsp.util.GVSPanel

class MenuButtonListener(val presenter: MainPresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_logo -> presenter.showHideAdminBar()
            R.id.btn_volumePannel -> presenter.selectPannel(GVSPanel.VOLUME)
            R.id.btn_eqPannel -> presenter.selectPannel(GVSPanel.EQ)
            R.id.btn_musicPlayer -> presenter.selectPannel(GVSPanel.MUSIC)
            R.id.btn_settings -> presenter.selectPannel(GVSPanel.SETTINGS)
        }
    }
}