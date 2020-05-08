package com.gvkorea.gvs1000_dsp.fragment.eq.listener

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.gvkorea.gvs1000_dsp.fragment.eq.presenter.GEQPresenter

class GEQItemSelectedListener(val presenter: GEQPresenter): AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        presenter.loadGEQ()
    }
}