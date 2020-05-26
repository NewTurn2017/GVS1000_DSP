package com.gvkorea.gvs1000_dsp.fragment.settings.view.preset.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.preset.presenter.PresetPresenter

class PresetButtonListener(val presenter: PresetPresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_presetSave -> presenter.presetSaveDialog()
            R.id.btn_presetLoad -> presenter.makeSavedListLoadDialog()
            R.id.btn_presetDelete -> presenter.makeSavedListDeleteDialog()
            R.id.btn_presetRequest -> presenter.makeSavedListRequestDialog()
        }
    }
}