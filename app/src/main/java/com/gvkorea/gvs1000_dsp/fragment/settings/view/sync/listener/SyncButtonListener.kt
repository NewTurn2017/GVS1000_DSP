package com.gvkorea.gvs1000_dsp.fragment.settings.view.sync.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.fragment.settings.view.sync.presenter.SyncPresenter

class SyncButtonListener(val presenter: SyncPresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        presenter.syncSettings()
    }
}