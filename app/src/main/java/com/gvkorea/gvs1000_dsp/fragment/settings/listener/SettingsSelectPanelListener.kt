package com.gvkorea.gvs1000_dsp.fragment.settings.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.util.GVSSettingPannel
import com.gvkorea.gvs1000_dsp.fragment.settings.presenter.SettingsPresenter

class SettingsSelectPanelListener(val presenter: SettingsPresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_ID_setting -> presenter.selectPanel(GVSSettingPannel.ID_SETTING)
            R.id.btn_resetSettings -> presenter.selectPanel(GVSSettingPannel.RESET_SETTINGS)
            R.id.btn_setMode -> presenter.selectPanel(GVSSettingPannel.SET_MODE)
            R.id.btn_sync -> presenter.selectPanel(GVSSettingPannel.SYNC)
            R.id.btn_spk_setting -> presenter.selectPanel(GVSSettingPannel.SPK_SETTING)
        }
    }
}