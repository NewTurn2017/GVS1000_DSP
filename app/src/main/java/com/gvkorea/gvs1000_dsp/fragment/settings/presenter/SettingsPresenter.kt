package com.gvkorea.gvs1000_dsp.fragment.settings.presenter

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.SettingsFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.util.GVSSettingPannel
import com.gvkorea.gvs1000_dsp.util.replaceChild
import com.manojbhadane.QButton
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsPresenter(val view: SettingsFragment) {
    fun selectPanel(panel: GVSSettingPannel) {
        resetButtonColor()
        when(panel){
            GVSSettingPannel.ID_SETTING -> replacePanel(view.setidFragment, view.btn_ID_setting)
            GVSSettingPannel.RESET_SETTINGS -> replacePanel(view.resetSettingFragment, view.btn_resetSettings)
            GVSSettingPannel.SPK_SETTING -> replacePanel(view.spkSettingsFragment, view.btn_spk_setting)
            GVSSettingPannel.SYNC -> replacePanel(view.syncFragment, view.btn_sync)
            GVSSettingPannel.SET_MODE -> replacePanel(view.setModeFragment, view.btn_setMode)
        }
    }

    private fun replacePanel(fragment: Fragment, button: QButton) {
        view.replaceChild(R.id.container_settings, fragment)
        setButtonColor(button, android.R.color.holo_red_light)
    }

    private fun resetButtonColor() {
        setButtonColor(view.btn_ID_setting, android.R.color.white)
        setButtonColor(view.btn_resetSettings, android.R.color.white)
        setButtonColor(view.btn_spk_setting, android.R.color.white)
        setButtonColor(view.btn_sync, android.R.color.white)
        setButtonColor(view.btn_setMode, android.R.color.white)
    }

    fun setButtonColor(button: QButton, color: Int) {
        button.setTextColor(ContextCompat.getColor(view.context!!, color))
    }

}