package com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting.listener

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting.presenter.SpkSettingsPresenter

class SpkSettingsItemSelectedListener(val presenter: SpkSettingsPresenter): AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        presenter.loadSavedData(position)
    }
}